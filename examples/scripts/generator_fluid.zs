// https://docs.blamejared.com/
// https://www.curseforge.com/minecraft/mc-mods/crafttweaker
// use /ct dump fluids to see all fluid IDs

var generator = <recipetype:cyclic:generator_fluid>;

generator.removeRecipe("cyclic:generator/generate_xp");
generator.removeRecipe("cyclic:generator/generate_lava");



generator.addRecipe("zoldo", <fluid:minecraft:water>*250, 5, 10);

generator.addRecipe("lava_tag", "minecraft:lava", 1000, 200, 500);
