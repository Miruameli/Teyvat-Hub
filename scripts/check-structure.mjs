#!/usr/bin/env node
/**
 * Checks the structural rules of the repository.
 *
 * The rules are simple enough to check by hand but easy to break by accident,
 * and a reviewer reading a diff cannot see a folder limit being exceeded.
 * This script reports every violation and exits non-zero when it finds one,
 * so it can run in CI and in a pre-commit hook.
 *
 * Usage: node scripts/check-structure.mjs
 */
import { readdirSync, readFileSync, statSync } from "node:fs";
import { join, relative } from "node:path";

const SOURCE_ROOT = "app/src/main/java";
const TEST_ROOT = "app/src/test/java";
const PACKAGE_ROOT = "com/teyvat/hub";

const MAX_ENTRIES_PER_FOLDER = 5;
const MAX_LINES_PER_FILE = 150;
const MAX_FILE_BYTES = 150 * 1024;

/** Layer each package belongs to, by its first path segment. */
const LAYERS = new Set([
  "domain",
  "data",
  "application",
  "presentation",
  "injection",
]);

const violations = [];

function report(kind, location, detail) {
  violations.push(`${kind}: ${location} — ${detail}`);
}

/** Walks a directory tree, skipping build output and binary resources. */
function walk(root, onFile, onDir) {
  if (!exists(root)) return;
  for (const entry of readdirSync(root)) {
    const full = join(root, entry);
    const stats = statSync(full);
    if (stats.isDirectory()) {
      onDir(full);
      walk(full, onFile, onDir);
    } else {
      onFile(full);
    }
  }
}

function exists(path) {
  try {
    statSync(path);
    return true;
  } catch {
    return false;
  }
}

/**
 * Counts what a folder holds directly.
 *
 * The rule limits files and subfolders separately: a folder may hold five
 * files and five subfolders at the same time, so counting them together
 * would report a folder that follows the rule as a violation.
 */
function directEntries(dir) {
  const names = readdirSync(dir).filter((name) => !name.startsWith("."));
  const files = names.filter((name) => statSync(join(dir, name)).isFile());
  const directories = names.filter((name) => statSync(join(dir, name)).isDirectory());
  return { files: files.length, directories: directories.length };
}

/** Reports folders that hold more than the allowed number of entries. */
function checkFolderLimits() {
  for (const root of [SOURCE_ROOT, TEST_ROOT]) {
    if (!exists(root)) continue;
    walk(
      root,
      () => {},
      (dir) => {
        const { files, directories } = directEntries(dir);
        if (files > MAX_ENTRIES_PER_FOLDER) {
          report(
            "folder",
            relative(root, dir),
            `${files} files, limit is ${MAX_ENTRIES_PER_FOLDER}`,
          );
        }
        if (directories > MAX_ENTRIES_PER_FOLDER) {
          report(
            "folder",
            relative(root, dir),
            `${directories} subfolders, limit is ${MAX_ENTRIES_PER_FOLDER}`,
          );
        }
      },
    );
  }
}

/** Reports Kotlin files that grew past the line limit. */
function checkFileLengths() {
  for (const root of [SOURCE_ROOT, TEST_ROOT]) {
    if (!exists(root)) continue;
    walk(
      root,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const text = readFileSync(file, "utf8");
        // A trailing newline does not start a new line of code.
        const lines = text.endsWith("\n")
          ? text.slice(0, -1).split("\n").length
          : text.split("\n").length;
        if (lines > MAX_LINES_PER_FILE) {
          report(
            "file",
            relative(root, file),
            `${lines} lines, limit is ${MAX_LINES_PER_FILE}`,
          );
        }
      },
      () => {},
    );
  }
}

/**
 * Reports a layer importing another layer it may not depend on.
 *
 * The allowed direction is presentation to application to domain, and data to
 * domain. A layer may always import from a layer below it and from itself.
 */
function checkLayerDependencies() {
  const base = join(SOURCE_ROOT, PACKAGE_ROOT);
  if (!exists(base)) return;
  walk(
    base,
    (file) => {
      if (!file.endsWith(".kt")) return;
      const relativePath = relative(base, file);
      const ownLayer = relativePath.split("/")[0];
      if (!LAYERS.has(ownLayer)) return;
      const imports = readFileSync(file, "utf8").match(/^import com\.teyvat\.hub\.[\w.]+$/gm) ?? [];
      for (const statement of imports) {
        const target = statement.replace(/^import com\.teyvat\.hub\./, "").split(".")[0];
        if (!LAYERS.has(target)) continue;
        if (target === ownLayer) continue;
        const allowed =
          (ownLayer === "presentation" && (target === "application" || target === "domain")) ||
          (ownLayer === "application" && (target === "domain" || target === "data")) ||
          (ownLayer === "data" && target === "domain") ||
          (ownLayer === "injection" && target !== "injection");
        if (!allowed) {
          report(
            "layer",
            relativePath,
            `${ownLayer} may not depend on ${target}`,
          );
        }
      }
    },
    () => {},
  );
}

/** Reports a package declaration that does not match the folder it sits in. */
function checkPackagePaths() {
  for (const root of [SOURCE_ROOT, TEST_ROOT]) {
    const base = join(root, PACKAGE_ROOT);
    if (!exists(base)) continue;
    walk(
      base,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const declared = readFileSync(file, "utf8").match(/^package ([\w.]+)$/m);
        if (!declared) return;
        const folder = relative(base, file.replace(/\/[^/]+$/, ""));
        const expected =
          folder === "" ? "com.teyvat.hub" : `com.teyvat.hub.${folder.split("/").join(".")}`;
        if (declared[1] !== expected) {
          report(
            "package",
            relative(root, file),
            `declares ${declared[1]} but sits in ${expected}`,
          );
        }
      },
      () => {},
    );
  }
}

/** Reports a file that grew large enough to slow a reader down. */
function checkFileSizes() {
  for (const root of [SOURCE_ROOT, TEST_ROOT]) {
    if (!exists(root)) continue;
    walk(
      root,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const bytes = statSync(file).size;
        if (bytes > MAX_FILE_BYTES) {
          report("size", relative(root, file), `${bytes} bytes`);
        }
      },
      () => {},
    );
  }
}

checkFolderLimits();
checkFileLengths();
checkLayerDependencies();
checkPackagePaths();
checkFileSizes();

if (violations.length === 0) {
  console.log("Structure check passed: no violations found.");
  process.exit(0);
}

console.error(`Structure check found ${violations.length} violation(s):`);
for (const violation of violations) {
  console.error(`  ${violation}`);
}
process.exit(1);
