import re

with open('src/main/java/com/lothrazar/cyclic/registry/MaterialRegistry.java', 'r') as f:
    content = f.read()

# Just replace the whole ToolMats class
new_toolmats = """  public static class ToolMats {
    public static final Tier NETHERBRICK = net.minecraft.world.item.Tiers.GOLD;
    public static final Tier SANDSTONE = net.minecraft.world.item.Tiers.STONE;
    public static final Tier COPPER = net.minecraft.world.item.Tiers.IRON;
    public static final Tier AMETHYST = net.minecraft.world.item.Tiers.IRON;
    public static final Tier EMERALD = net.minecraft.world.item.Tiers.DIAMOND;
    public static final Tier GEMOBSIDIAN = net.minecraft.world.item.Tiers.NETHERITE;
  }
}
"""

content = re.sub(r'public static class ToolMats \{.*', new_toolmats, content, flags=re.DOTALL)

with open('src/main/java/com/lothrazar/cyclic/registry/MaterialRegistry.java', 'w') as f:
    f.write(content)
