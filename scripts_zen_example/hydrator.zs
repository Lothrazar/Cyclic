import mods.cyclicmagic.Hydrator;

/**
Inputs must be of size 1 or 4, size 1 means shapeless
*/
Hydrator.addRecipe(<minecraft:cobblestone> * 2, [<minecraft:gravel> * 4], 10);


/**
Inputs must be of size 1 or 4. size 4 is shaped
*/
Hydrator.addRecipe(<minecraft:gravel>, [<minecraft:sand>,<minecraft:sand>,<minecraft:sand>,<minecraft:sand>], 10);


/**
Remove 2x2 recipe
*/
Hydrator.removeShapedRecipe(<minecraft:packed_ice>);

/**
Remove shapeless recipe
*/
Hydrator.removeShapelessRecipe(<minecraft:farmland>);
