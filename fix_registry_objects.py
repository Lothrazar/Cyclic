import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'RegistryObject<' in content:
        content = content.replace('RegistryObject<', 'java.util.function.Supplier<')
        modified = True
        
    if 'import net.neoforged.neoforge.registries.RegistryObject;' in content:
        content = content.replace('import net.neoforged.neoforge.registries.RegistryObject;\n', '')
        modified = True

    if 'ForgeRegistries.RECIPE_SERIALIZERS' in content:
        content = content.replace('ForgeRegistries.RECIPE_SERIALIZERS', 'Registries.RECIPE_SERIALIZER')
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
