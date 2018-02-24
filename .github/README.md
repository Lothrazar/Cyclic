
# Cyclic [![](http://cf.way2muchnoise.eu/239286.svg)](https://minecraft.curseforge.com/projects/cyclic) [![](http://cf.way2muchnoise.eu/versions/239286.svg)](https://minecraft.curseforge.com/projects/cyclic)

Minecraft mod written in Java using the Minecraft Forge API.  MIT License.


Releases, information, and screenshots on Curse: http://mods.curse.com/mc-mods/minecraft/239286-cyclic


# SETUP

- First make sure vanilla minecraft is installed on your machine, and run it at least once in the current minecraft version (see gradle.properties : mc_version)
- run the following bash command in the root folder
`gradlew setupDecompWorkspace eclipse`
- This uses the build.gradle script and uses the forge MDK
- If you have any issues, you may have to download the MDK on your own from http://files.minecraftforge.net/ 
- Finally, in eclipse go to File - Import - Existing Projects - and select the folder


# Contributions

- To help with a bug, please check the Issues tab, especially the ones labelled "stuck"
- Language files are also welcome via Pull Request, currently suppored are the following https://github.com/PrinceOfAmber/Cyclic/tree/master/src/main/resources/assets/cyclicmagic/lang


# APIs and Optional Dependencies
#### I have plugins/compatibility with the following mods, they are highly recommended.  However everything works perfectly fine without them.

- Baubles https://mods.curse.com/mc-mods/minecraft/227083-baubles
- Guide API https://mods.curse.com/mc-mods/minecraft/228832-guide-api
- JEI https://mods.curse.com/mc-mods/minecraft/238222-just-enough-items-jei
- Enchantment Descriptions https://minecraft.curseforge.com/projects/enchantment-descriptions

## CraftTweaker support

Recipes for the Hydrator can be customized (added and removed) using CraftTwaker
- https://minecraft.curseforge.com/projects/crafttweaker
- Sample script https://github.com/PrinceOfAmber/Cyclic/blob/master/scripts_zen_example/hydrator.zs


