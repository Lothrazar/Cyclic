import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'net.minecraftforge.registries.' in content:
        content = content.replace('net.minecraftforge.registries.', 'net.neoforged.neoforge.registries.')
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
