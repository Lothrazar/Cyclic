import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    # Fix initCapabilities in specific files
    if 'public ICapabilityProvider initCapabilities' in content:
        content = content.replace('public ICapabilityProvider initCapabilities', '// public Object initCapabilities')
        modified = True
        
    # Fix NeoForge common/event imports
    if 'net.minecraftforge.common.' in content:
        content = content.replace('net.minecraftforge.common.', 'net.neoforged.neoforge.common.')
        modified = True
        
    if 'net.minecraftforge.event.' in content:
        content = content.replace('net.minecraftforge.event.', 'net.neoforged.neoforge.event.')
        modified = True
        
    if 'net.minecraftforge.api.distmarker.' in content:
        content = content.replace('net.minecraftforge.api.distmarker.', 'net.neoforged.api.distmarker.')
        modified = True
        
    if 'net.minecraftforge.client.' in content:
        content = content.replace('net.minecraftforge.client.', 'net.neoforged.neoforge.client.')
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
