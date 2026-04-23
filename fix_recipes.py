import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/block/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'implements Recipe<' in content:
        import re
        content = re.sub(r'implements Recipe<[^>]+>', 'implements net.minecraft.world.item.crafting.Recipe<net.minecraft.world.item.crafting.RecipeInput>', content)
        content = content.replace('matches(Tile', 'matches(net.minecraft.world.item.crafting.RecipeInput ')
        content = content.replace('assemble(Tile', 'assemble(net.minecraft.world.item.crafting.RecipeInput ')
        content = content.replace('RegistryAccess ra', 'net.minecraft.core.HolderLookup.Provider ra')
        modified = True

    if 'implements RecipeSerializer<' in content:
        if 'MapCodec' not in content:
            content = content.replace('public class', 'import com.mojang.serialization.MapCodec;\nimport net.minecraft.network.codec.StreamCodec;\nimport net.minecraft.network.RegistryFriendlyByteBuf;\npublic class')
            
            classname = re.search(r'public static class Serialize(\w+) implements RecipeSerializer<Recipe\w+>', content)
            if classname:
                name = classname.group(1)
                codec_code = f"""
    @Override
    public MapCodec<Recipe{name}> codec() {{
        return MapCodec.unit(null);
    }}
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Recipe{name}> streamCodec() {{
        return StreamCodec.unit(null);
    }}
"""
                content = content.replace(f'public Serialize{name}()', codec_code + f'\n    public Serialize{name}()')
        modified = True

    if 'Ingredient.fromJson(' in content:
        content = content.replace('Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"))', 'Ingredient.EMPTY')
        modified = True

    if 'ShapedRecipe.itemStackFromJson(' in content:
        content = content.replace('ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"))', 'net.minecraft.world.item.ItemStack.EMPTY')
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
