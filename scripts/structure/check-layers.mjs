/**
 * Checks that no layer imports a layer it may not depend on.
 *
 * The allowed direction is presentation to application to domain, and data to
 * domain. A layer may always import from itself and from a layer below it.
 */
import { readFileSync } from "node:fs";
import { join } from "node:path";
import {
  PACKAGE_ROOT,
  ROOTS,
  TEST_ROOT,
  layerOf,
  layerOfImport,
  packageOf,
  projectPath,
  walk,
} from "./checks-common.mjs";

/** Which layers a layer is allowed to reach in the main source. */
const ALLOWED = {
  presentation: ["application", "domain"],
  application: ["domain", "data"],
  data: ["domain"],
  injection: ["domain", "data", "application", "presentation"],
};

/**
 * Which layers a test is allowed to reach.
 *
 * A test may use anything it needs to set up the state it checks, including
 * the data layer, so the rules are wider there than in the main source.
 */
const ALLOWED_IN_TESTS = {
  presentation: ["application", "domain", "data"],
  application: ["domain", "data"],
  data: ["domain"],
  injection: ["domain", "data", "application", "presentation"],
};

/**
 * Reports every import that crosses a layer boundary it may not cross.
 *
 * The roots are a parameter so a test can point the check at a temporary
 * tree instead of the real source, which keeps the test from touching the
 * project it checks.
 */
export function checkLayerDependencies(roots = ROOTS) {
  const violations = [];
  for (const root of roots) {
    const allowed = root === TEST_ROOT ? ALLOWED_IN_TESTS : ALLOWED;
    const base = join(root, PACKAGE_ROOT);
    walk(
      base,
      (file) => {
        if (!file.endsWith(".kt")) return;
        // The layer is the segment after the package root, not of the path:
        // the path starts with the source root, which says nothing about the
        // layer the file belongs to.
        const declared = packageOf(file);
        if (!declared) return;
        const ownLayer = layerOf(declared);
        if (!ownLayer) return;
        const text = readFileSync(file, "utf8");
        const imports = text.match(/^import com\.teyvat\.hub\.[\w.]+$/gm) ?? [];
        for (const statement of imports) {
          const target = layerOfImport(statement);
          if (!target || target === ownLayer) continue;
          if (!(allowed[ownLayer] ?? []).includes(target)) {
            violations.push(
              `layer: ${projectPath(file)} — ${ownLayer} may not depend on ${target}`,
            );
          }
        }
      },
      () => {},
    );
  }
  return violations;
}
