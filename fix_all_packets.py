import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    if 'PacketFlib' in content or 'NetworkEvent' in content:
        # replace extends PacketFlib
        content = content.replace('extends PacketFlib', 'implements net.minecraft.network.protocol.common.custom.CustomPacketPayload')
        
        # replace Context parameter
        content = content.replace('Supplier<NetworkEvent.Context> ctx', 'net.neoforged.neoforge.network.handling.IPayloadContext ctx')
        content = content.replace('ctx.get().enqueueWork', 'ctx.enqueueWork')
        content = content.replace('ctx.enqueueWork()(()', 'ctx.enqueueWork(()')
        content = content.replace('ctx.get().setPacketHandled', '// ctx.setPacketHandled')
        content = content.replace('ctx.get().getSender()', 'ctx.player()')
        content = re.sub(r'message\.done\(ctx\);?', '', content)
        content = content.replace('ServerPlayer sender = ctx.player();', 'net.minecraft.server.level.ServerPlayer sender = (net.minecraft.server.level.ServerPlayer) ctx.player();')
        
        # Add Type and type() method
        class_name_match = re.search(r'public class (\w+)', content)
        if class_name_match:
            class_name = class_name_match.group(1)
            snake_case_name = re.sub(r'(?<!^)(?=[A-Z])', '_', class_name).lower()
            
            # Check if TYPE is already added
            if 'public static final Type<' + class_name + '> TYPE' not in content:
                type_code = f"""
  public static final Type<{class_name}> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "{snake_case_name}"));

  @Override
  public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {{
    return TYPE;
  }}
"""
                content = re.sub(rf'(public class {class_name}[^{{]*{{)', r'\1\n' + type_code, content)
            
            # Check if STREAM_CODEC is already added
            if 'decode(' in content and 'STREAM_CODEC' not in content:
                codec_code = f"""
  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, {class_name}> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of({class_name}::encode, {class_name}::decode);
"""
                content = re.sub(rf'(public static final Type<{class_name}> TYPE =[^;]+;)', r'\1\n' + codec_code, content)
        
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
