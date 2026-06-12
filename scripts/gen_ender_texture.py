"""
Generate ender_still.png and ender_flow.png by palette-swapping the honey
fluid textures to use the color palette of the teleport block texture.
Crops output to 16x384 (24 frames at frametime=5).
"""

from PIL import Image

OUTLINE_LUMA = 40

def luma(c):
  r, g, b = c
  return 0.299 * r + 0.587 * g + 0.114 * b

def unique_colors(img):
  px = img.load()
  w, h = img.size
  seen = {}
  for y in range(h):
    for x in range(w):
      c = px[x, y]
      if c[3] < 128:
        continue
      seen[c[:3]] = seen.get(c[:3], 0) + 1
  return seen

def build_ramp(colors_dict):
  items = [c for c in colors_dict if luma(c) >= OUTLINE_LUMA]
  items.sort(key=luma)
  return items

def make_mapping(src_ramp, donor_ramp):
  n = len(src_ramp)
  m = len(donor_ramp)
  mapping = {}
  for i, c in enumerate(src_ramp):
    if n == 1:
      idx = 0
    else:
      idx = round(i * (m - 1) / (n - 1))
    mapping[c] = donor_ramp[idx]
  return mapping

def apply_mapping(src_img, mapping, out_height):
  w, _ = src_img.size
  out = Image.new("RGBA", (w, out_height))
  sp = src_img.load()
  op = out.load()
  for y in range(out_height):
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
        op[x, y] = (r, g, b, a)
  return out

ASSETS = "src/main/resources/assets/cyclic/textures"

honey_still = Image.open(f"{ASSETS}/block/fluid/honey_still.png").convert("RGBA")
honey_flow = Image.open(f"{ASSETS}/block/fluid/honey_flow.png").convert("RGBA")
teleport = Image.open(f"{ASSETS}/block/eye/teleport.png").convert("RGBA")

src_colors = unique_colors(honey_still)
donor_colors = unique_colors(teleport)

src_ramp = build_ramp(src_colors)
donor_ramp = build_ramp(donor_colors)

print(f"src ramp ({len(src_ramp)}): {src_ramp[:5]}...")
print(f"donor ramp ({len(donor_ramp)}): {donor_ramp}")

mapping = make_mapping(src_ramp, donor_ramp)

print(f"Built mapping with {len(mapping)} entries")
for k, v in list(mapping.items())[:6]:
  print(f"  {k} -> {v}")

out_still = apply_mapping(honey_still, mapping, 384)
out_still.save(f"{ASSETS}/block/fluid/ender_still.png")
print("wrote ender_still.png")

out_flow = apply_mapping(honey_flow, mapping, 384)
out_flow.save(f"{ASSETS}/block/fluid/ender_flow.png")
print("wrote ender_flow.png")
