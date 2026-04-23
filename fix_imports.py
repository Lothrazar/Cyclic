import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    if 'net.minecraftforge.network.NetworkEvent' in content:
        content = re.sub(r'import net\.minecraftforge\.network\.NetworkEvent;\n', '', content)
        modified = True
    if 'net.minecraftforge.network.NetworkHooks' in content:
        content = re.sub(r'import net\.minecraftforge\.network\.NetworkHooks;\n', '', content)
        modified = True
    if 'import com.lothrazar.library.packet.PacketFlib;' in content:
        content = re.sub(r'import com\.lothrazar\.library\.packet\.PacketFlib;\n', '', content)
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
