"""
Palette-swap a source texture using colors from a donor texture.
Strategy:
  - Read source (emerald_boots) and donor (copper_sword).
  - Build a brightness-ordered ramp of unique opaque colors in each.
  - Map source ramp -> donor ramp by relative position (interpolated index).
  - Outline / near-black pixels in the source are KEPT AS-IS (per user request).
  - Transparent pixels stay transparent.

  python scripts/palette_swap.py <source.png> <donor.png> <output.png> [drop_dark=0] [gr_cutoff=0.6]
"""

from PIL import Image
import sys


SRC = sys.argv[1]
DONOR = sys.argv[2]
OUT = sys.argv[3]
# Optional 4th arg: skip this many of the darkest donor colors (bias toward bright end).
DROP_DARK = int(sys.argv[4]) if len(sys.argv) > 4 else 0
# Optional 5th arg: G/R upper cutoff for "is orange" filter (default 0.6).
GR_CUTOFF = float(sys.argv[5]) if len(sys.argv) > 5 else 0.6

# Pixels with luma below this are treated as "outline" and not remapped.
OUTLINE_LUMA = 60

def luma(rgba):
  r, g, b, a = rgba
  return 0.299 * r + 0.587 * g + 0.114 * b

def unique_opaque_colors(img):
  px = img.load()
  w, h = img.size
  seen = {}
  for y in range(h):
    for x in range(w):
      c = px[x, y]
      if c[3] == 0:
        continue
      seen[c[:3]] = seen.get(c[:3], 0) + 1
  return seen

def saturation(rgb):
  r, g, b = rgb
  mx, mn = max(r, g, b), min(r, g, b)
  if mx == 0:
    return 0
  return (mx - mn) / mx

def ramp(colors_dict, drop_outline=False, min_saturation=0.0):
  # Returns list of (r,g,b) sorted dark -> light.
  items = list(colors_dict.keys())
  if drop_outline:
    items = [c for c in items if luma((*c, 255)) >= OUTLINE_LUMA]
  if min_saturation > 0:
    # For copper-like ramps, also drop colors where green channel is too high
    # relative to red (those read as brown/wood, not orange copper).
    def is_orange(c):

      r, g, b = c
      if r == 0:
        return False
      return (g / r) < GR_CUTOFF
    items = [c for c in items if saturation(c) >= min_saturation and is_orange(c)]
  items.sort(key=lambda c: luma((*c, 255)))
  return items

src_img = Image.open(SRC).convert("RGBA")
donor_img = Image.open(DONOR).convert("RGBA")

src_colors = unique_opaque_colors(src_img)
donor_colors = unique_opaque_colors(donor_img)

# Source ramp without outline (outline is preserved unchanged).
src_ramp = ramp(src_colors, drop_outline=True)
# Donor ramp also drops its outline so we map color -> color, not color -> black.
donor_ramp = ramp(donor_colors, drop_outline=True, min_saturation=0.4)
if DROP_DARK > 0:
  donor_ramp = donor_ramp[DROP_DARK:]

print(f"src ramp ({len(src_ramp)}):", src_ramp)
print(f"donor ramp ({len(donor_ramp)}):", donor_ramp)

 # Build mapping: each source color -> donor color at proportional index.
mapping = {}
n_src = len(src_ramp)
n_donor = len(donor_ramp)
for i, c in enumerate(src_ramp):
  if n_src == 1:
    idx = 0
  else:
    idx = round(i * (n_donor - 1) / (n_src - 1))
  mapping[c] = donor_ramp[idx]

print("mapping:")
for k, v in mapping.items():
  print(f"  {k} -> {v}")

out = Image.new("RGBA", src_img.size)
sp = src_img.load()
op = out.load()
w, h = src_img.size
for y in range(h):
  for x in range(w):
    r, g, b, a = sp[x, y]
    if a == 0:
      op[x, y] = (0, 0, 0, 0)
      continue
    key = (r, g, b)
    if key in mapping:
      nr, ng, nb = mapping[key]
      op[x, y] = (nr, ng, nb, a)
    else:
      # Outline / not-remapped: keep as-is.
      op[x, y] = (r, g, b, a)

out.save(OUT)
print(f"wrote {OUT}")
