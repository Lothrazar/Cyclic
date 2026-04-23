import os

filepath = 'src/main/java/com/lothrazar/cyclic/item/storagebag/PacketStorageBagScreen.java'
with open(filepath, 'r') as f:
    content = f.read()

content = content.replace('net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, PacketStorageBagScreen>', 'net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketStorageBagScreen>')
content = content.replace('public static PacketStorageBagScreen decode(FriendlyByteBuf buffer)', 'public static PacketStorageBagScreen decode(net.minecraft.network.RegistryFriendlyByteBuf buffer)')
content = content.replace('public static void encode(PacketStorageBagScreen message, FriendlyByteBuf buffer)', 'public static void encode(PacketStorageBagScreen message, net.minecraft.network.RegistryFriendlyByteBuf buffer)')

content = content.replace('packet.stack = buffer.readItem();', 'packet.stack = ItemStack.STREAM_CODEC.decode(buffer);')
content = content.replace('buffer.writeItem(message.stack);', 'ItemStack.STREAM_CODEC.encode(buffer, message.stack);')

# Fix serverStack tag
content = content.replace('serverStack.getOrCreateTag().put(key, message.nbtValue);', 'net.minecraft.nbt.CompoundTag tag = ItemStorageBag.getCustomData(serverStack); tag.put(key, message.nbtValue); ItemStorageBag.setCustomData(serverStack, tag);')

with open(filepath, 'w') as f:
    f.write(content)
