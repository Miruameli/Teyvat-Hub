#!/usr/bin/env node
/**
 * Builds a CycloneDX 1.5 SBOM from the pinned version catalog.
 *
 * The catalog is the source of truth for every dependency version, and every
 * entry is pinned to an exact version, so the SBOM can be derived from it
 * without resolving anything from a network. That keeps the output
 * deterministic and the release build free of an extra Gradle plugin.
 *
 * Usage: node scripts/generate-sbom.mjs <output.json>
 */
import { readFileSync, writeFileSync } from "node:fs";
import { join } from "node:path";

const CATALOG_PATH = "gradle/libs.versions.toml";
const BUILD_FILE = "app/build.gradle.kts";
const COMPONENT_NAME = "Teyvat-Hub";

/** Reads a quoted or bare value from a TOML assignment. */
function tomlValue(raw) {
  const value = raw.trim();
  const quoted = value.match(/^"([^"]*)"$/);
  if (quoted) return quoted[1];
  return value;
}

/**
 * Parses the catalog into version aliases and library entries.
 *
 * The parser covers the subset of TOML the catalog actually uses: section
 * headers, `key = "value"` assignments, and `key = { ... }` inline tables
 * whose values are either a literal or a `{ group = "x", name = "y" }` table.
 */
function parseCatalog(text) {
  const versions = new Map();
  const libraries = [];

  let section = "";
  for (const rawLine of text.split("\n")) {
    const line = rawLine.trim();
    if (line === "" || line.startsWith("#")) continue;

    const sectionMatch = line.match(/^\[([^\]]+)\]$/);
    if (sectionMatch) {
      section = sectionMatch[1];
      continue;
    }

    const assignment = line.match(/^([A-Za-z0-9_-]+)\s*=\s*(.+)$/);
    if (!assignment) continue;

    const [, key, rawValue] = assignment;

    if (section === "versions") {
      versions.set(key, tomlValue(rawValue));
      continue;
    }

    if (section !== "libraries") continue;

    const inline = rawValue.match(/^\{(.*)\}$/);
    if (!inline) continue;

    const body = inline[1];
    const group = body.match(/group\s*=\s*"([^"]+)"/);
    const name = body.match(/name\s*=\s*"([^"]+)"/);
    if (!group || !name) continue;

    // The version reference must be matched first: a plain /version\s*=/ also
    // matches "version.ref", which silently resolved every pinned library to
    // the wrong version.
    const versionRef = body.match(/version\.ref\s*=\s*"([^"]+)"/);
    const versionLiteral = body.match(/(?:^|[^.\w])version\s*=\s*"([^"]+)"/);
    let version = null;
    if (versionRef) {
      version = versions.get(versionRef[1]) ?? null;
    } else if (versionLiteral) {
      version = versionLiteral[1];
    }

    if (!version) {
      // Version managed by a BOM or a plugin, not by this catalog. Recorded
      // explicitly so the SBOM never hides a component it cannot pin.
      libraries.push({
        alias: key,
        group: group[1],
        name: name[1],
        version: null,
        managedBy: "bom",
      });
      continue;
    }

    libraries.push({ alias: key, group: group[1], name: name[1], version });
  }

  return { versions, libraries };
}

/** Reads the application version so the SBOM identifies the exact build. */
function readAppVersion() {
  const buildFile = readFileSync(BUILD_FILE, "utf8");
  const versionName = buildFile.match(/versionName\s*=\s*"([^"]+)"/);
  const versionCode = buildFile.match(/versionCode\s*=\s*(\d+)/);
  return {
    version: versionName ? versionName[1] : "unknown",
    versionCode: versionCode ? Number(versionCode[1]) : 0,
  };
}

/** Package URL for a Maven coordinate, the standard identifier format. */
function purl(group, name, version) {
  return `pkg:maven/${group}/${name}@${version}`;
}

function main() {
  const outputPath = process.argv[2];
  if (!outputPath) {
    console.error("Usage: node scripts/generate-sbom.mjs <output.json>");
    process.exit(1);
  }

  const catalog = parseCatalog(readFileSync(CATALOG_PATH, "utf8"));
  const app = readAppVersion();

  const pinned = catalog.libraries.filter((library) => library.version);
  const managed = catalog.libraries.filter((library) => !library.version);

  const components = [
    ...pinned.map((library) => ({
      type: "library",
      "bom-ref": purl(library.group, library.name, library.version),
      group: library.group,
      name: library.name,
      version: library.version,
      purl: purl(library.group, library.name, library.version),
    })),
    // A BOM-managed version is not claimed here. CycloneDX allows a component
    // without a version, and a distinct bom-ref keeps the document valid; the
    // origin of the version is stated in the component properties.
    ...managed.map((library) => ({
      type: "library",
      "bom-ref": `pkg:maven/${library.group}/${library.name}?version-managed`,
      group: library.group,
      name: library.name,
      properties: [
        { name: "teyvat:versionSource", value: "gradle version catalog or platform BOM" },
      ],
    })),
  ].sort((a, b) => a["bom-ref"].localeCompare(b["bom-ref"]));

  const bom = {
    bomFormat: "CycloneDX",
    specVersion: "1.5",
    version: 1,
    metadata: {
      component: {
        type: "application",
        "bom-ref": `pkg:generic/${COMPONENT_NAME.toLowerCase()}@${app.version}`,
        name: COMPONENT_NAME,
        version: app.version,
      },
      properties: [
        { name: "teyvat:versionCode", value: String(app.versionCode) },
        { name: "teyvat:sbomSource", value: CATALOG_PATH },
        {
          name: "teyvat:versionResolution",
          value:
            "components with a version are pinned in the version catalog; " +
            "components without one are managed by a platform BOM and are " +
            "marked with a version-managed bom-ref",
        },
      ],
    },
    components,
  };

  writeFileSync(outputPath, `${JSON.stringify(bom, null, 2)}\n`);

  const refs = new Set(components.map((c) => c["bom-ref"]));
  if (refs.size !== components.length) {
    console.error("ERROR: duplicate bom-ref values in the generated SBOM");
    process.exit(1);
  }

  console.log(
    `SBOM written to ${outputPath} (app ${app.version}, ` +
      `${pinned.length} pinned + ${managed.length} BOM-managed components)`,
  );
  if (managed.length > 0) {
    console.log("BOM-managed components (version not claimed):");
    managed.forEach((library) => console.log(`  - ${library.alias} (${library.name})`));
  }
}

main();
