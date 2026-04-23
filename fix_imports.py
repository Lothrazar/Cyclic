import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    imports = []
    # Find all imports
    for i, line in enumerate(lines):
        if line.startswith("import ") and not line.startswith("import static "):
            # Extract the class name
            match = re.search(r'import\s+[\w\.]+\.(\w+);', line)
            if match:
                classname = match.group(1)
                imports.append((i, classname, line))
    
    if not imports:
        return False
        
    # Join file content for searching
    # We remove the import lines so they don't count as "usage"
    content_without_imports = ""
    for i, line in enumerate(lines):
        is_import = False
        for imp_idx, _, _ in imports:
            if i == imp_idx:
                is_import = True
                break
        if not is_import:
            content_without_imports += line

    lines_to_delete = set()
    for imp_idx, classname, line in imports:
        # Search for whole word match of the classname
        if not re.search(r'\b' + classname + r'\b', content_without_imports):
            lines_to_delete.add(imp_idx)
            
    if not lines_to_delete:
        return False
        
    with open(filepath, 'w', encoding='utf-8') as f:
        for i, line in enumerate(lines):
            if i not in lines_to_delete:
                f.write(line)
                
    return len(lines_to_delete)

total_removed = 0
for root, dirs, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            removed = process_file(os.path.join(root, file))
            if removed:
                total_removed += removed

print(f"Removed {total_removed} unused imports!")
