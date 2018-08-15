import mods.cyclicmagic.Hydrator;
import crafttweaker.item.IItemStack;
// https://crafttweaker.readthedocs.io/en/latest

//Remove recipe
Hydrator.removeShapedRecipe(<minecraft:packed_ice>);

//Add recipe.  Between 1 and 4 ingredients, and then the fluid amount
val IArray as IItemStack[] = [<minecraft:ice>, <minecraft:ice>, <minecraft:ice>, <minecraft:ice>];
Hydrator.addRecipe(<minecraft:dirt>, IArray, 10);

