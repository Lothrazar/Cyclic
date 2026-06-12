"""
gen_fluid_wave.py - Generate animated fluid textures from a source block texture.

Produces a 16x384 animated PNG (24 frames x 16px) with a sinusoidal wave
shimmer effect. Pass the same image for both still and flow, or omit flow to
skip it.

Usage:
  python scripts/gen_fluid_wave.py <source.png> <out_still.png> [out_flow.png] [amplitude=2.0]

  source.png  - any 16xN block texture (tiled/cropped to 16x16 base)
  amplitude   - wave strength in pixels (default 2.0, try 1.0 for subtle)

Example:
  python scripts/gen_fluid_wave.py libs/amethyst_block.png block/fluid/amethyst_still.png block/fluid/amethyst_flow.png
"""

import sys
import math
from PIL import Image

SRC = sys.argv[1]
OUT_STILL = sys.argv[2]
OUT_FLOW = sys.argv[3] if len(sys.argv) > 3 and not sys.argv[3].startswith("amp") else None
AMPLITUDE = float(sys.argv[4]) if len(sys.argv) > 4 else (float(sys.argv[3]) if len(sys.argv) > 3 and sys.argv[3].replace(".", "").isdigit() else 2.0)

FRAMES = 24
FRAME_SIZE = 16
OUT_HEIGHT = FRAMES * FRAME_SIZE  # 384

src = Image.open(SRC).convert("RGBA")
sw, sh = src.size

# Build a 16x16 base tile by tiling the source (handles multi-row sources like sculk)
tile = Image.new("RGBA", (FRAME_SIZE, FRAME_SIZE))
for y in range(FRAME_SIZE):
  for x in range(FRAME_SIZE):
    sx = x % sw
    sy = y % sh
    tile.putpixel((x, y), src.getpixel((sx, sy)))


def make_wave(amplitude):
  out = Image.new("RGBA", (FRAME_SIZE, OUT_HEIGHT))
  for fi in range(FRAMES):
    for row in range(FRAME_SIZE):
      shift = round(amplitude * math.sin(
        2 * math.pi * fi / FRAMES + 2 * math.pi * row / FRAME_SIZE
      ))
      for col in range(FRAME_SIZE):
        src_col = (col - shift) % FRAME_SIZE
        pixel = tile.getpixel((src_col, row))
        out.putpixel((col, fi * FRAME_SIZE + row), pixel)
  return out


still = make_wave(AMPLITUDE)
still.save(OUT_STILL)
print(f"wrote {OUT_STILL}")

if OUT_FLOW:
  flow = make_wave(AMPLITUDE)
  flow.save(OUT_FLOW)
  print(f"wrote {OUT_FLOW}")
