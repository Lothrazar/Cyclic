import mods.cyclicmagic.Hydrator;
import crafttweaker.item.IItemStack;
// https://crafttweaker.readthedocs.io/en/latest

//Remove recipe
Hydrator.removeShapedRecipe(<minecraft:packed_ice>);
Hydrator.removeShapedRecipe(<minecraft:grass>);
Hydrator.removeShapedRecipe(<minecraft:grass_path>);
Hydrator.removeShapedRecipe(<minecraft:clay_ball>);
Hydrator.removeShapedRecipe(<minecraft:concrete>);



//Add recipe.  Between 1 and 4 ingredients, and then the fluid amount
val IArray as IItemStack[] = [<minecraft:ice>, <minecraft:ice>, <minecraft:ice>, <minecraft:ice>];
Hydrator.addRecipe(<minecraft:dirt>, IArray, 10);

