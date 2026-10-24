/**
 * Checks that no Kotlin file grew past the line limit or the size limit.
 */
import { readFileSync, statSync } from "node:fs";
import {
  MAX_FILE_BYTES,
  MAX_LINES_PER_FILE,
  ROOTS,
  lineCount,
  projectPath,
  walk,
} from "./checks-common.mjs";

export function checkFileLengths() {
  const violations = [];
  for (const root of ROOTS) {
    walk(
      root,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const lines = lineCount(readFileSync(file, "utf8"));
        if (lines > MAX_LINES_PER_FILE) {
          violations.push(
            `file: ${projectPath(file)} — ${lines} lines, limit is ${MAX_LINES_PER_FILE}`,
          );
        }
      },
      () => {},
    );
  }
  return violations;
}

export function checkFileSizes() {
  const violations = [];
  for (const root of ROOTS) {
    walk(
      root,
      (file) => {
        if (!file.endsWith(".kt")) return;
        const bytes = statSync(file).size;
        if (bytes > MAX_FILE_BYTES) {
          violations.push(`size: ${projectPath(file)} — ${bytes} bytes`);
        }
      },
      () => {},
    );
  }
  return violations;
}
