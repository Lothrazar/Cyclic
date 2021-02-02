

// examples of removing melter recipe

// this removes resources\data\cyclic\recipes\solidifier_conc_cyan.json

<recipetype:cyclic:solidifier>.removeRecipe("cyclic:solidifier_conc_cyan");

// add recipe with name, 3 input ingredients, input fluid, output item

<recipetype:cyclic:solidifier>.addRecipe("change_this_everytime", <item:minecraft:gravel>, <item:minecraft:dirt>, <item:minecraft:cobblestone>, <fluid:minecraft:lava> * 200, <item:minecraft:stone> * 4);
 