#!/usr/bin/env bash
# Renders the launcher icon PNGs for API 24 and 25 from the master artwork.
#
# API 26 and above use the adaptive icon in mipmap-anydpi-v26, which is
# defined in XML and needs no raster. Older launchers only understand PNG, so
# the same emblem is rendered here at every density.
#
# The master artwork lives in docs/branding/ic_launcher_source.svg and must
# not be placed in app/src/main/res: AAPT2 does not accept SVG resources and
# the build would fail.
#
# Usage: scripts/render-launcher-icons.sh
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SOURCE="$REPO_ROOT/docs/branding/ic_launcher_source.svg"
RES_DIR="$REPO_ROOT/app/src/main/res"

if [[ ! -f "$SOURCE" ]]; then
  echo "ERROR: master artwork missing at $SOURCE"
  exit 1
fi

if ! command -v convert >/dev/null 2>&1; then
  echo "ERROR: ImageMagick 'convert' is required to render the launcher icons"
  exit 1
fi

# density:size in pixels
DENSITIES=("mdpi 48" "hdpi 72" "xhdpi 96" "xxhdpi 144" "xxxhdpi 192")

for entry in "${DENSITIES[@]}"; do
  read -r density size <<<"$entry"
  target_dir="$RES_DIR/mipmap-$density"
  mkdir -p "$target_dir"

  convert -background none -density 400 "$SOURCE" \
    -resize "${size}x${size}" "$target_dir/ic_launcher.png"

  # The round variant is the same artwork clipped to a circle.
  convert -background none -density 400 "$SOURCE" \
    -resize "${size}x${size}" \
    \( -size "${size}x${size}" xc:none -fill white \
       -draw "circle $((size / 2)),$((size / 2)) $((size / 2)),0" \) \
    -compose CopyOpacity -composite "$target_dir/ic_launcher_round.png"

  echo "rendered mipmap-$density at ${size}x${size}"
done

echo "Launcher icons regenerated. Verify the sky colour with:"
echo "  convert $RES_DIR/mipmap-xxxhdpi/ic_launcher.png -format '%[pixel:p{5,5}]' info:"
