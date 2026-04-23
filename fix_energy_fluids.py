import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'net.minecraftforge.energy.' in content:
        content = content.replace('net.minecraftforge.energy.', 'net.neoforged.neoforge.energy.')
        modified = True
        
    if 'net.minecraftforge.fluids.' in content:
        content = content.replace('net.minecraftforge.fluids.', 'net.neoforged.neoforge.fluids.')
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
