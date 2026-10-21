#!/usr/bin/env node
/**
 * Checks the structural rules of the repository.
 *
 * The rules are simple enough to check by hand but easy to break by accident,
 * and a reviewer reading a diff cannot see a folder limit being exceeded.
 * This script reports every violation and exits non-zero when it finds one,
 * so it can run in CI and in a pre-commit hook.
 *
 * The individual checks live in ./structure/ and share the helpers there.
 *
 * Usage: node scripts/check-structure.mjs
 */
import { checkFolderLimits } from "./structure/check-folders.mjs";
import { checkFileLengths, checkFileSizes } from "./structure/check-files.mjs";
import { checkLayerDependencies } from "./structure/check-layers.mjs";
import { checkPackagePaths } from "./structure/check-packages.mjs";

const checks = [
  checkFolderLimits,
  checkFileLengths,
  checkFileSizes,
  checkLayerDependencies,
  checkPackagePaths,
];

const violations = [];
for (const check of checks) {
  violations.push(...check());
}

if (violations.length === 0) {
  console.log("Structure check passed: no violations found.");
  process.exit(0);
}

console.error(`Structure check found ${violations.length} violation(s):`);
for (const violation of violations) {
  console.error(`  ${violation}`);
}
process.exit(1);
