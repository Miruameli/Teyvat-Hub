/**
 * Checks that no folder holds more than the allowed number of files or
 * subfolders.
 */
import {
  MAX_ENTRIES_PER_FOLDER,
  ROOTS,
  directEntries,
  projectPath,
  walk,
} from "./checks-common.mjs";

export function checkFolderLimits() {
  const violations = [];
  for (const root of ROOTS) {
    walk(
      root,
      () => {},
      (dir) => {
        const { files, directories } = directEntries(dir);
        if (files > MAX_ENTRIES_PER_FOLDER) {
          violations.push(
            `folder: ${projectPath(dir)} — ${files} files, limit is ${MAX_ENTRIES_PER_FOLDER}`,
          );
        }
        if (directories > MAX_ENTRIES_PER_FOLDER) {
          violations.push(
            `folder: ${projectPath(dir)} — ${directories} subfolders, limit is ${MAX_ENTRIES_PER_FOLDER}`,
          );
        }
      },
    );
  }
  return violations;
}
