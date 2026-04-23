import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'net.minecraftforge.items' in content:
        content = content.replace('net.minecraftforge.items', 'net.neoforged.neoforge.items')
        modified = True
        
    if 'import net.minecraftforge.common.capabilities.ForgeCapabilities;' in content:
        content = content.replace('import net.minecraftforge.common.capabilities.ForgeCapabilities;', 'import net.neoforged.neoforge.capabilities.Capabilities;')
        modified = True
        
    if 'Supplier<NetworkEvent.Context>' in content:
        content = content.replace('Supplier<NetworkEvent.Context>', 'net.neoforged.neoforge.network.handling.IPayloadContext')
        modified = True
        
    if 'MagmaFluidBlock.java' in f:
        if 'import net.neoforged.neoforge.fluids.BaseFlowingFluid;' not in content:
            content = content.replace('import net.minecraft.world.level.block.LiquidBlock;', 'import net.minecraft.world.level.block.LiquidBlock;\nimport net.neoforged.neoforge.fluids.BaseFlowingFluid;')
            modified = True
            
    if modified:
        with open(f, 'w') as file:
            file.write(content)
