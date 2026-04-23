import os, glob, re

NET_DIR = 'src/main/java/com/lothrazar/cyclic/net'

for f in glob.glob(f'{NET_DIR}/*.java'):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    # Replace FriendlyByteBuf with RegistryFriendlyByteBuf in StreamCodec declarations
    new_content = content
    
    # Fix StreamCodec<FriendlyByteBuf, ...> -> StreamCodec<RegistryFriendlyByteBuf, ...>
    new_content = new_content.replace(
        'StreamCodec<net.minecraft.network.FriendlyByteBuf,',
        'StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf,'
    )
    new_content = new_content.replace(
        'StreamCodec<FriendlyByteBuf,',
        'StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf,'
    )
    
    # Fix decode(FriendlyByteBuf buf) -> decode(RegistryFriendlyByteBuf buf)  
    new_content = new_content.replace(
        'decode(FriendlyByteBuf buf)',
        'decode(net.minecraft.network.RegistryFriendlyByteBuf buf)'
    )
    new_content = new_content.replace(
        'decode(net.minecraft.network.FriendlyByteBuf buf)',
        'decode(net.minecraft.network.RegistryFriendlyByteBuf buf)'
    )
    
    # Fix encode(Packet msg, FriendlyByteBuf buf) -> encode(Packet msg, RegistryFriendlyByteBuf buf)
    new_content = re.sub(
        r'encode\((\w+) msg, FriendlyByteBuf buf\)',
        r'encode(\1 msg, net.minecraft.network.RegistryFriendlyByteBuf buf)',
        new_content
    )
    new_content = re.sub(
        r'encode\((\w+) msg, net\.minecraft\.network\.FriendlyByteBuf buf\)',
        r'encode(\1 msg, net.minecraft.network.RegistryFriendlyByteBuf buf)',
        new_content
    )
    
    # Fix NetworkHooks.openScreen -> PlatformHelper or just direct call in 1.21
    new_content = new_content.replace(
        'NetworkHooks.openScreen(',
        '((net.minecraft.server.level.ServerPlayer) ctx.player()).openMenu('
    )
    
    if new_content != content:
        with open(f, 'w') as file:
            file.write(new_content)
        print(f"Fixed: {f}")
