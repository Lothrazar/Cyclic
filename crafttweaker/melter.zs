

// examples of removing melter recipes

<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_bamboo");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_exp");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_expblaze");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_expbone");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_expflesh");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_expghast");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_expwitherrose");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_fbio");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_honey");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_honeybottle");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_honeycomb");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_kelp");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_magma");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_magmacream");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_obslava");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_seeds");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_slimeballs");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_slimeblock");
<recipetype:cyclic:melter>.removeRecipe("cyclic:melter_snowwater");

// not removed
// 
// <recipetype:cyclic:melter>.removeRecipe("cyclic:melter_icetowater");
// 
 
// create water from these two
<recipetype:cyclic:melter>.addRecipe("ct_water", <item:minecraft:stick>, <item:minecraft:dirt>, <fluid:minecraft:water> * 222);
 
// get some lava
<recipetype:cyclic:melter>.addRecipe("ct_lava", <item:minecraft:stick>, <item:minecraft:grass_block>, <fluid:minecraft:lava> * 333);
 
 