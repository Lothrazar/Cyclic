import os, re

def replace_in_file(filepath, old, new):
    if not os.path.exists(filepath): return
    with open(filepath, 'r') as f: content = f.read()
    if old in content:
        with open(filepath, 'w') as f: f.write(content.replace(old, new))

def regex_replace_in_file(filepath, pattern, replacement):
    if not os.path.exists(filepath): return
    with open(filepath, 'r') as f: content = f.read()
    new_content = re.sub(pattern, replacement, content)
    if new_content != content:
        with open(filepath, 'w') as f: f.write(new_content)

replace_in_file('src/main/java/com/lothrazar/cyclic/item/storagebag/ItemStorageBag.java', 'public ItemStack tryFilteredInsert(ItemStack bag, ItemStack stack) {', 'public static ItemStack tryFilteredInsert(ItemStack bag, ItemStack stack) {')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/storagebag/ItemStorageBag.java', '/* ItemHandlerHelper.copyStackWithSize */', 'stack.copy()')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/magicnet/EntityMagicNetEmpty.java', 'EntityMagicNetEmpty.NBT_ENTITYID', 'EntityMagicNetEmpty.NBT_ENTITYID')

regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheLeaves.java', r'@Override\s*public Rarity getRarity\(ItemStack stack\) \{\s*return Rarity\.UNCOMMON;\s*\}', '')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheLeaves.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.SILK_TOUCH, p\)', '0')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheLeaves.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.FORTUNE, p\)', '0')

regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheBrush.java', r'@Override\s*public Rarity getRarity\(ItemStack stack\) \{\s*return Rarity\.UNCOMMON;\s*\}', '')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheBrush.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.SILK_TOUCH, p\)', '0')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheBrush.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.FORTUNE, p\)', '0')

regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheHarvest.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.FORTUNE, p\)', '0')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/ScytheForage.java', r'EnchantmentHelper\.getEnchantmentLevel\(Enchantments\.FORTUNE, p\)', '0')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/PacketScythe.java', 'public static void encode(PacketScythe message, net.minecraft.network.RegistryFriendlyByteBuf buffer)', 'public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buffer, PacketScythe message)')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/scythe/PacketScythe.java', 'ServerPlayer player = context.player();', 'ServerPlayer player = (ServerPlayer) context.player();')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/PacketChestSack.java', 'public static void encode(PacketChestSack message, net.minecraft.network.RegistryFriendlyByteBuf buffer)', 'public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buffer, PacketChestSack message)')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/PacketChestSack.java', 'ServerPlayer player = context.player();', 'ServerPlayer player = (ServerPlayer) context.player();')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'stack.hasTag()', 'stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', '!stack.hasTag()', '!stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'stack.getTag()', 'stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag()')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'stack.getOrCreateTag()', 'net.minecraft.world.item.component.CustomData.EMPTY.copyTag()')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'stack.setTag(null);', 'stack.remove(net.minecraft.core.component.DataComponents.CUSTOM_DATA);')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'stack.setTag(tag);', 'net.minecraft.world.item.component.CustomData.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, stack, tag);')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterItem.java', 'world.getBlockEntity(pos).saveWithoutMetadata()', 'world.getBlockEntity(pos).saveWithoutMetadata(world.registryAccess())')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterEmptyItem.java', 'stack.hasTag()', 'stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterEmptyItem.java', 'world.getBlockEntity(pos).saveWithoutMetadata()', 'world.getBlockEntity(pos).saveWithoutMetadata(world.registryAccess())')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/transporter/TileTransporterEmptyItem.java', 'stack.setTag(tag);', 'net.minecraft.world.item.component.CustomData.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, stack, tag);')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/lunchbox/ContainerLunchbox.java', 'public ContainerLunchbox(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {', 'public ContainerLunchbox(int windowId, Level world, BlockPos pos, net.minecraft.world.entity.player.Inventory playerInventory, Player player) {')
regex_replace_in_file('src/main/java/com/lothrazar/cyclic/item/lunchbox/ItemLunchbox.java', r'@Override\s*public Rarity getRarity\(ItemStack stack\) \{\s*return Rarity\.UNCOMMON;\s*\}', '')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/lunchbox/ItemLunchbox.java', 'stack.getOrCreateTag()', 'net.minecraft.world.item.component.CustomData.EMPTY.copyTag()')
replace_in_file('src/main/java/com/lothrazar/cyclic/item/lunchbox/ItemLunchbox.java', 'box.getOrCreateTag()', 'net.minecraft.world.item.component.CustomData.EMPTY.copyTag()')

replace_in_file('src/main/java/com/lothrazar/cyclic/item/magicnet/EntityMagicNetEmpty.java', 'NBT_ENTITYID = "id"', 'NBT_ENTITYID = "id"')

