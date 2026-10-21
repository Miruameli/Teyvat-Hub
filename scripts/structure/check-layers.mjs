/**
 * Checks that no layer imports a layer it may not depend on.
 *
 * The allowed direction is presentation to application to domain, and data to
 * domain. A layer may always import from itself and from a layer below it.
 */
import { readFileSync } from "node:fs";
import { join } from "node:path";
import {
  LAYERS,
  PACKAGE_ROOT,
  ROOTS,
  SOURCE_ROOT,
  packageOf,
  projectPath,
  walk,
} from "./checks-common.mjs";

/** Which layers a layer is allowed to reach. */
const ALLOWED = {
  presentation: ["application", "domain"],
  application: ["domain", "data"],
  data: ["domain"],
  injection: ["domain", "data", "application", "presentation"],
};

export function checkLayerDependencies() {
  const violations = [];
  const base = join(SOURCE_ROOT, PACKAGE_ROOT);
  walk(
    base,
    (file) => {
      if (!file.endsWith(".kt")) return;
      const ownLayer = projectPath(file).split("/")[0];
      if (!LAYERS.has(ownLayer)) return;
      const text = readFileSync(file, "utf8");
      const imports = text.match(/^import com\.teyvat\.hub\.[\w.]+$/gm) ?? [];
      for (const statement of imports) {
        const target = statement.replace(/^import com\.teyvat\.hub\./, "").split(".")[0];
        if (!LAYERS.has(target) || target === ownLayer) continue;
        if (!(ALLOWED[ownLayer] ?? []).includes(target)) {
          violations.push(
            `layer: ${projectPath(file)} — ${ownLayer} may not depend on ${target}`,
          );
        }
      }
    },
    () => {},
  );
  return violations;
}
