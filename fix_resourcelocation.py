import os, glob, re

for f in glob.glob('src/main/java/com/lothrazar/cyclic/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'new ResourceLocation(' in content:
        # replace new ResourceLocation(a, b) with ResourceLocation.fromNamespaceAndPath(a, b)
        new_content = re.sub(r'new ResourceLocation\(([^,]+), ([^)]+)\)', r'ResourceLocation.fromNamespaceAndPath(\1, \2)', content)
        # replace new ResourceLocation(a) with ResourceLocation.parse(a)
        new_content = re.sub(r'new ResourceLocation\(([^,)]+)\)', r'ResourceLocation.parse(\1)', new_content)
        
        if new_content != content:
            content = new_content
            modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
