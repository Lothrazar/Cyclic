import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'ForgeFlowingFluid' in content:
        content = content.replace('net.minecraftforge.fluids.ForgeFlowingFluid', 'net.neoforged.neoforge.fluids.BaseFlowingFluid')
        content = content.replace('ForgeFlowingFluid', 'BaseFlowingFluid')
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
