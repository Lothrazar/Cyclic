import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/block/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    # We want to replace "net.minecraft.world.item.crafting.RecipeInput Crusher inv" with "net.minecraft.world.item.crafting.RecipeInput inv"
    new_content = re.sub(r'net\.minecraft\.world\.item\.crafting\.RecipeInput\s+[A-Za-z]+\s+([a-zA-Z0-9_]+)', r'net.minecraft.world.item.crafting.RecipeInput \1', content)
    
    if new_content != content:
        content = new_content
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
