import os, glob
for f in glob.glob('src/main/java/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    if 'com.lothrazar.library.cap.CustomEnergyStorage' in content:
        content = content.replace('com.lothrazar.library.cap.CustomEnergyStorage', 'com.lothrazar.cyclic.capabilities.CustomEnergyStorage')
        with open(f, 'w') as file:
            file.write(content)
