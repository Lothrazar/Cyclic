import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'LazyOptional' in content or 'Capability<' in content or 'ICapabilityProvider' in content or 'ICapabilitySerializable' in content:
        # comment out imports
        content = re.sub(r'import net\.minecraftforge\.common\.capabilities\.[^;]+;\n', '', content)
        content = re.sub(r'import net\.minecraftforge\.common\.util\.LazyOptional;\n', '', content)
        
        # comment out LazyOptional variable declarations
        content = re.sub(r'([ \t]*)(.*LazyOptional.*)', r'\1// \2', content)
        
        # comment out getCapability overrides (naive approach: comment out the method signature)
        # We also need to comment out the body. This is risky, but we can replace getCapability with a renamed method
        content = content.replace('public <T> LazyOptional<T> getCapability', '// public <T> LazyOptional<T> getCapability')
        content = content.replace('@Override\n  // public <T> LazyOptional<T> getCapability', '// public <T> LazyOptional<T> getCapability')
        
        # Replace implements ICapabilityProvider with nothing
        content = content.replace('implements ICapabilityProvider, INBTSerializable<CompoundTag>', 'implements net.neoforged.neoforge.common.util.INBTSerializable<net.minecraft.nbt.CompoundTag>')
        content = content.replace('implements ICapabilitySerializable<CompoundTag>', '')
        
        modified = True
        
    if modified:
        with open(f, 'w') as file:
            file.write(content)
