// https://docs.blamejared.com/
// https://www.curseforge.com/minecraft/mc-mods/crafttweaker
// use /ct dump fluids to see all fluid IDs

var melter = <recipetype:cyclic:melter>;

melter.removeRecipe("cyclic:melter/melter_snowwater");
melter.removeRecipe("cyclic:melter/melter_expflesh", "cyclic:melter/melter_expblaze");

melter.addRecipe("spruce_wayne", [<item:minecraft:sand>,<item:minecraft:spruce_trapdoor>], <fluid:cyclic:wax>*75, 500, 3);