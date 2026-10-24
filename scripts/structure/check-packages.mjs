/**
 * Checks that every package declaration matches the folder the file sits in.
 *
 * A file whose package says one thing while its folder says another is a
 * mismatch that the compiler accepts, so it is only caught by this check.
 */
import { join, relative } from "node:path";
import {
  PACKAGE_ROOT,
  ROOTS,
  packageOf,
  projectPath,
  walk,
} from "./checks-common.mjs";

export function checkPackagePaths() {
  const violations = [];
  for (const root of ROOTS) {
    const base = join(root, PACKAGE_ROOT);
    walk(
      base,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const declared = packageOf(file);
        if (!declared) return;
        const folder = relative(base, file.replace(/\/[^/]+$/, ""));
        const expected =
          folder === "" ? "com.teyvat.hub" : `com.teyvat.hub.${folder.split("/").join(".")}`;
        if (declared !== expected) {
          violations.push(
            `package: ${projectPath(file)} — declares ${declared} but sits in ${expected}`,
          );
        }
      },
      () => {},
    );
  }
  return violations;
}
