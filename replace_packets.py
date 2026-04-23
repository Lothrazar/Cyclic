import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/net/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    if 'PacketFlib' in content or 'NetworkEvent' in content:
        # replace extends PacketFlib
        content = content.replace('extends PacketFlib', 'implements net.minecraft.network.protocol.common.custom.CustomPacketPayload')
        
        # replace Context parameter
        content = content.replace('Supplier<NetworkEvent.Context> ctx', 'net.neoforged.neoforge.network.handling.IPayloadContext ctx')
        content = content.replace('ctx.get().enqueueWork', 'ctx.enqueueWork()')
        content = content.replace('ctx.get().setPacketHandled', '// ctx.setPacketHandled')
        content = content.replace('ctx.get().getSender()', 'ctx.player()')
        
        # Add Type and type() method
        class_name_match = re.search(r'public class (\w+)', content)
        if class_name_match:
            class_name = class_name_match.group(1)
            snake_case_name = re.sub(r'(?<!^)(?=[A-Z])', '_', class_name).lower()
            
            type_code = f"""
  public static final Type<{class_name}> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "{snake_case_name}"));

  @Override
  public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {{
    return TYPE;
  }}
"""
            # insert after class declaration
            content = re.sub(rf'(public class {class_name}[^{{]*{{)', r'\1\n' + type_code, content)
            
        with open(f, 'w') as file:
            file.write(content)
