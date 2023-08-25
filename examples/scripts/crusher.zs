// https://docs.blamejared.com/
// https://www.curseforge.com/minecraft/mc-mods/crafttweaker
// use /ct dump fluids to see all fluid IDs

var crusher = <recipetype:cyclic:crusher>;

crusher.removeRecipe("cyclic:crusher/nylium_w");
crusher.removeRecipe("cyclic:crusher/end_crystal", "cyclic:crusher/netherrack");

// ID, input, output, RF per tick, ticks, bonus, percentage of bonus 
crusher.addRecipe("castlevania", <item:minecraft:diamond>,<item:minecraft:iron_nugget>*2, 500, 3, <item:minecraft:dirt>*2, 50);


// no bonus, same as last number zero
crusher.addRecipePlain("castlevania2", <item:minecraft:obsidian>,<item:minecraft:sand>*2, 500, 300);


