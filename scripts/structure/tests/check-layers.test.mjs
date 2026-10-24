/**
 * Verifies that the layer check actually detects a violation.
 *
 * A gate that quietly stops looking at anything passes forever, and this check
 * was a no-op twice because of a path mistake. The probe lives in a temporary
 * tree, so the test never touches the source it checks.
 */
import { strict as assert } from "node:assert";
import { mkdirSync, mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { dirname, join } from "node:path";
import { test } from "node:test";
import { checkLayerDependencies } from "../check-layers.mjs";

/**
 * Builds a throwaway source tree and runs the check against it.
 *
 * The tree is removed afterwards, so the project is never modified.
 */
function withTree(files, run) {
  const root = mkdtempSync(join(tmpdir(), "structure-check-"));
  try {
    for (const [relativePath, content] of Object.entries(files)) {
      const full = join(root, relativePath);
      mkdirSync(dirname(full), { recursive: true });
      writeFileSync(full, content);
    }
    return run(root);
  } finally {
    rmSync(root, { recursive: true, force: true });
  }
}

test("the layer check reports a forbidden import", () => {
  const violations = withTree(
    {
      "com/teyvat/hub/presentation/feature/probe/Probe.kt": [
        "package com.teyvat.hub.presentation.feature.probe",
        "",
        "import com.teyvat.hub.data.local.team.TeamDao",
        "",
        "class Probe",
      ].join("\n"),
    },
    (root) => checkLayerDependencies([root]),
  );

  assert.ok(
    violations.some((violation) => violation.includes("may not depend on data")),
    `expected a layer violation, got: ${JSON.stringify(violations)}`,
  );
});

test("the layer check accepts a permitted import", () => {
  const violations = withTree(
    {
      "com/teyvat/hub/presentation/feature/probe/AllowedProbe.kt": [
        "package com.teyvat.hub.presentation.feature.probe",
        "",
        "import com.teyvat.hub.domain.team.Team",
        "",
        "class AllowedProbe",
      ].join("\n"),
    },
    (root) => checkLayerDependencies([root]),
  );

  assert.deepEqual(violations, []);
});
