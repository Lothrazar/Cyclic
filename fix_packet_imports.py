import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    # Remove old forge network imports
    if 'net.minecraftforge.network.NetworkEvent' in content:
        content = re.sub(r'import net\.minecraftforge\.network\.NetworkEvent;\n', '', content)
        modified = True
    if 'net.minecraftforge.network.NetworkHooks' in content:
        content = re.sub(r'import net\.minecraftforge\.network\.NetworkHooks;\n', '', content)
        modified = True
        
    # Replace Type with CustomPacketPayload.Type
    if 'Type<' in content and 'import net.minecraft.network.protocol.common.custom.CustomPacketPayload;' not in content:
        # Since I used Type<BlockFacadeMessage> instead of CustomPacketPayload.Type<BlockFacadeMessage>, I will fully qualify it
        content = content.replace('public static final Type<', 'public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<')
        content = content.replace('public Type<?', 'public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<?')
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
