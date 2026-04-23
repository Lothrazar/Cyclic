import re

with open('src/main/java/com/lothrazar/cyclic/registry/ItemRegistry.java', 'r') as f:
    content = f.read()

# Swords (damage 3, speed -2.4F)
content = re.sub(r'new SwordItem\((.*?), new Item.Properties\(\)\)', r'new SwordItem(\1, new Item.Properties().attributes(SwordItem.createAttributes(\1, 3, -2.4F)))', content)

# Pickaxes (damage 1, speed -2.8F)
content = re.sub(r'new PickaxeItem\((.*?), new Item.Properties\(\)\)', r'new PickaxeItem(\1, new Item.Properties().attributes(PickaxeItem.createAttributes(\1, 1, -2.8F)))', content)

# Axes (damage 6.0F, speed -3.1F)
content = re.sub(r'new AxeItem\((.*?), new Item.Properties\(\)\)', r'new AxeItem(\1, new Item.Properties().attributes(AxeItem.createAttributes(\1, 6.0F, -3.1F)))', content)

# Shovels (damage 1.5F, speed -3.0F)
content = re.sub(r'new ShovelItem\((.*?), new Item.Properties\(\)\)', r'new ShovelItem(\1, new Item.Properties().attributes(ShovelItem.createAttributes(\1, 1.5F, -3.0F)))', content)

# Hoes (damage -4, speed 0F) - wait, HoeItem takes negative damage modifier relative to the tier.
content = re.sub(r'new HoeItem\((.*?), new Item.Properties\(\)\)', r'new HoeItem(\1, new Item.Properties().attributes(HoeItem.createAttributes(\1, -4, 0F)))', content)

with open('src/main/java/com/lothrazar/cyclic/registry/ItemRegistry.java', 'w') as f:
    f.write(content)
