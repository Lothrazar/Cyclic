// https://docs.blamejared.com/
// https://www.curseforge.com/minecraft/mc-mods/crafttweaker
// use /ct dump fluids to see all fluid IDs

var solidifier = <recipetype:cyclic:solidifier>;

solidifier.removeRecipe("cyclic:solidifier/candle_blue");
solidifier.removeRecipe("cyclic:solidifier/candle_red", "cyclic:solidifier/candle_yellow");

// this one is the exact fluid ID, not using tags
solidifier.addRecipe("megaman1", [<item:minecraft:sand>,<item:minecraft:birch_trapdoor>,<item:minecraft:sand>], <fluid:cyclic:honey>*100, <item:minecraft:stone_hoe>, 200, 8);

// next: string-tag format using strings instead of a fluid
// run /ct dump tag 
// to see all registered fluid tags

solidifier.addRecipe("megaman2", [<item:minecraft:sand>,<item:minecraft:spruce_trapdoor>,<item:minecraft:sand>], "forge:wax", 100, <item:minecraft:iron_hoe>, 300, 100);

solidifier.addRecipe("megaman3", [<item:minecraft:sand>,<item:minecraft:oak_trapdoor>,<item:minecraft:sand>], "minecraft:lava", 100, <item:minecraft:iron_sword>, 300, 100);

solidifier.addRecipe("megaman4", [<item:minecraft:sand>,<item:minecraft:jungle_trapdoor>,<item:minecraft:sand>], "<fluid:minecraft:lava>", 5000, <item:minecraft:obsidian>, 300, 100);

