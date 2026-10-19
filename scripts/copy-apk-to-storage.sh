#!/usr/bin/env bash
# Places the freshly built debug APK at the repository root, ready to be copied
# to /sdcard by the user. Run from the repo root after a successful build.
set -euo pipefail

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
DEST="TeyvatHub-debug.apk"

if [[ ! -f "$APK_PATH" ]]; then
  echo "ERROR: APK not found at $APK_PATH"
  echo "Run ./gradlew :app:assembleDebug first."
  exit 1
fi

cp "$APK_PATH" "./$DEST"

echo "APK ready at: $(pwd)/$DEST"
ls -lh "./$DEST"
