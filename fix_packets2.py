import os, glob, re

NET_DIR = 'src/main/java/com/lothrazar/cyclic/net'

for f in glob.glob(f'{NET_DIR}/*.java'):
    with open(f, 'r') as file:
        content = file.read()
    
    new_content = content
    
    # The issue: StreamCodec.of(encoder, decoder) in 1.21 has signature:
    # StreamCodec.of(StreamEncoder<B,V> encoder, StreamDecoder<B,V> decoder)
    # where StreamEncoder is (B buf, V value) -> void  (buf FIRST)
    # and StreamDecoder is (B buf) -> V
    # But our encode methods are (Packet msg, Buf buf) -> void  (msg FIRST = old Forge style)
    # We need to swap to (Buf buf, Packet msg) -> void
    
    # Find encode methods with (Msg msg, RegistryFriendlyByteBuf buf) signature and swap them
    # Pattern: public static void encode(TypeName msg, net.minecraft.network.RegistryFriendlyByteBuf buf)
    new_content = re.sub(
        r'public static void encode\((\w+) msg, net\.minecraft\.network\.RegistryFriendlyByteBuf buf\)',
        r'public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, \1 msg)',
        new_content
    )
    
    # Fix StreamCodec.of calls to swap encoder/decoder order
    # StreamCodec.of(Cls::encode, Cls::decode) -> the encoder goes first in 1.21 too,
    # but the encoder signature needs to be (buf, value) not (value, buf)
    # Since we already fixed encode signature above, this should be fine
    # BUT we also need to swap the lambda/method reference order in the call:
    # StreamCodec.of(Class::encode, Class::decode) stays the same
    # The issue is the encode method signature itself
    
    # Also fix: ctx.player() returns Player not ServerPlayer -> cast
    new_content = re.sub(
        r'ServerPlayer player = ctx\.player\(\);',
        r'ServerPlayer player = (ServerPlayer) ctx.player();',
        new_content
    )
    
    # Fix: NetworkHooks.openScreen already replaced but the method signature is wrong
    # openMenu doesn't take a ContainerProvider + BlockPos overload the same way
    # Replace with PlatformHooks.openMenu or leave as comment
    new_content = new_content.replace(
        '((net.minecraft.server.level.ServerPlayer) ctx.player()).openMenu(',
        'net.minecraft.server.level.ServerPlayer _sp = (net.minecraft.server.level.ServerPlayer) ctx.player(); _sp.openMenu('
    )
    
    if new_content != content:
        with open(f, 'w') as file:
            file.write(new_content)
        print(f"Fixed: {f}")
