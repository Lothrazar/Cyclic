// https://docs.blamejared.com/
// https://www.curseforge.com/minecraft/mc-mods/crafttweaker
// use /ct dump fluids to see all fluid IDs

var generator = <recipetype:cyclic:generator_item>;

// recipe IDS, not item ids
generator.removeRecipe("cyclic:generator/generate_redstone");
generator.removeRecipe("cyclic:generator/generate_star");

// ID, input, output, RF per tick, ticks, bonus, percentage of bonus 
generator.addRecipe("zelda", <item:minecraft:diamond_block>, 500, 120);
