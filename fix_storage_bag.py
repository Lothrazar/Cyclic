import os, re

filepath = 'src/main/java/com/lothrazar/cyclic/item/storagebag/ItemStorageBag.java'
with open(filepath, 'r') as f:
    content = f.read()

# Remove registerClient
content = re.sub(r'@OnlyIn\(Dist\.CLIENT\)\s*public void registerClient\(\) \{[\s\S]*?\}', '', content)

content = content.replace('stack.hasTag()', '!stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).isEmpty()')

# We'll just define a helper method to handle getting/setting
helper = """
  public static net.minecraft.nbt.CompoundTag getCustomData(ItemStack stack) {
    return stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
  }
  public static void setCustomData(ItemStack stack, net.minecraft.nbt.CompoundTag tag) {
    stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
  }
"""

if "getCustomData(ItemStack stack)" not in content:
    content = content.replace('public class ItemStorageBag extends ItemBaseCyclic {', 'public class ItemStorageBag extends ItemBaseCyclic {' + helper)

content = content.replace('stack.getOrCreateTag()', 'getCustomData(stack)')
content = content.replace('stack.getTag()', 'getCustomData(stack)')
content = content.replace('stack.setTag(', 'setCustomData(stack, ')

# Save
with open(filepath, 'w') as f:
    f.write(content)

# SleepingMatItem NBT
filepath = 'src/main/java/com/lothrazar/cyclic/item/SleepingMatItem.java'
if os.path.exists(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    content = content.replace('stack.getOrCreateTag()', 'stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag()')
    content = content.replace('stack.setTag(nbt);', 'stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(nbt));')
    content = content.replace('player.sleepCounter', 'player.getSleepTimer()') # guess
    with open(filepath, 'w') as f:
        f.write(content)

