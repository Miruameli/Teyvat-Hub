/**
 * Shared helpers for the structure checks.
 *
 * The checks themselves live in separate files so each one stays readable and
 * every file stays within the size limit.
 */
import { readdirSync, readFileSync, statSync } from "node:fs";
import { join, relative } from "node:path";

export const SOURCE_ROOT = "app/src/main/java";
export const TEST_ROOT = "app/src/test/java";
export const PACKAGE_ROOT = "com/teyvat/hub";

export const MAX_ENTRIES_PER_FOLDER = 5;
export const MAX_LINES_PER_FILE = 150;
export const MAX_FILE_BYTES = 150 * 1024;

/** Layer each package belongs to, by its first path segment. */
export const LAYERS = new Set([
  "domain",
  "data",
  "application",
  "presentation",
  "injection",
]);

/** True when the path exists. */
export function exists(path) {
  try {
    statSync(path);
    return true;
  } catch {
    return false;
  }
}

/** Walks a directory tree, calling the visitor for every file and folder. */
export function walk(root, onFile, onDir) {
  if (!exists(root)) return;
  for (const entry of readdirSync(root)) {
    const full = join(root, entry);
    if (statSync(full).isDirectory()) {
      onDir(full);
      walk(full, onFile, onDir);
    } else {
      onFile(full);
    }
  }
}

/** The source and test roots, in the order they are checked. */
export const ROOTS = [SOURCE_ROOT, TEST_ROOT];

/**
 * The package a file declares, or null when it declares none.
 */
export function packageOf(file) {
  const declared = readFileSync(file, "utf8").match(/^package ([\w.]+)$/m);
  return declared ? declared[1] : null;
}

/**
 * How many files and how many subfolders a folder holds directly.
 *
 * The rule limits the two separately: a folder may hold five files and five
 * subfolders at the same time.
 */
export function directEntries(dir) {
  const names = readdirSync(dir).filter((name) => !name.startsWith("."));
  const files = names.filter((name) => statSync(join(dir, name)).isFile());
  const directories = names.filter((name) => statSync(join(dir, name)).isDirectory());
  return { files: files.length, directories: directories.length };
}

/** The number of lines in a file, not counting a trailing newline. */
export function lineCount(text) {
  return text.endsWith("\n")
    ? text.slice(0, -1).split("\n").length
    : text.split("\n").length;
}

/** A path relative to the project root, for readable messages. */
export function projectPath(path) {
  return relative(".", path);
}

/** The package root every layer sits under. */
const PACKAGE_PREFIX = "com.teyvat.hub";

/**
 * The layer a package belongs to.
 *
 * The layer is the segment right after the package root, so
 * com.teyvat.hub.presentation.feature.roster belongs to presentation.
 */
export function layerOf(packageName) {
  if (!packageName.startsWith(PACKAGE_PREFIX)) return null;
  const rest = packageName.slice(PACKAGE_PREFIX.length + 1);
  if (rest === "") return null;
  const layer = rest.split(".")[0];
  return LAYERS.has(layer) ? layer : null;
}

/**
 * The layer an imported symbol belongs to.
 *
 * A symbol outside the package root, such as a library class, has no layer.
 */
export function layerOfImport(statement) {
  if (!statement.startsWith(`import ${PACKAGE_PREFIX}.`)) return null;
  const rest = statement.slice(`import ${PACKAGE_PREFIX}.`.length);
  const layer = rest.split(".")[0];
  return LAYERS.has(layer) ? layer : null;
}
