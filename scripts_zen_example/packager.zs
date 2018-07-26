import mods.cyclicmagic.Packager;
// https://crafttweaker.readthedocs.io/en/latest
// output, input
Packager.addRecipe(<minecraft:dirt>, <minecraft:grass>*9);

// output, multiple inputs
Packager.addRecipe(<minecraft:grass>, [<minecraft:dirt>*2, <minecraft:cobblestone>*3]);

// remove default recipes
Packager.removeRecipe(<minecraft:iron_ingot>);
Packager.removeRecipe(<minecraft:gold_ingot>);
