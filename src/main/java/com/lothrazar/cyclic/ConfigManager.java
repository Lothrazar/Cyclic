package com.lothrazar.cyclic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

  private static List<String> defaultBeheading = new ArrayList<>();
  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static BooleanValue SCAFFOLD = null;
  public static BooleanValue EMERALD;
  public static BooleanValue SANDSTONE;
  public static BooleanValue GEMGEAR;
  public static BooleanValue NETHERBRICK;
  public static BooleanValue ENCHANTMENTS;
  public static BooleanValue CHARMS;
  public static BooleanValue GLOVE;
  public static BooleanValue HEARTS;
  public static BooleanValue CARROTS;
  public static BooleanValue SPIKES;
  public static BooleanValue BOOMERANGS;
  public static BooleanValue CABLES;
  public static IntValue ANVILPOWER;
  public static IntValue MELTERPOWER;
  public static IntValue SOLIDIFIERPOWER;
  public static IntValue PEATERICHPOWER;
  public static IntValue PEATPOWER;
  public static IntValue HARVESTERPOWER;
  public static IntValue DISENCHANTERPOWER;
  public static DoubleValue PEATCHANCE;
  public static BooleanValue COMMANDNBT;
  public static BooleanValue COMMANDGETHOME;
  public static BooleanValue COMMANDHEALTH;
  public static BooleanValue COMMANDHOME;
  public static BooleanValue COMMANDHUNGER;
  public static BooleanValue COMMANDPINGNETHER;
  public static BooleanValue COMMANDWORLDSPAWN;
  public static BooleanValue COMMANDGETHELP;
  private static ConfigValue<List<String>> BEHEADING_SKINS;
  public static IntValue DROPPERPOWER;
  public static IntValue FORESTERPOWER;

  private static void buildDefaultHeadList() {
    //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
    //TODO config file for extra mod support 
    defaultBeheading.add("minecraft:blaze:MHF_Blaze");
    defaultBeheading.add("minecraft:cat:MHF_Ocelot");
    defaultBeheading.add("minecraft:cave_spider:MHF_CaveSpider");
    defaultBeheading.add("minecraft:chicken:MHF_Chicken");
    defaultBeheading.add("minecraft:cow:MHF_Cow");
    defaultBeheading.add("minecraft:enderman:MHF_Enderman");
    defaultBeheading.add("minecraft:ghast:MHF_Ghast");
    defaultBeheading.add("minecraft:iron_golem:MHF_Golem");
    defaultBeheading.add("minecraft:magma_cube:MHF_LavaSlime");
    defaultBeheading.add("minecraft:mooshroom:MHF_MushroomCow");
    defaultBeheading.add("minecraft:ocelot:MHF_Ocelot");
    defaultBeheading.add("minecraft:pig:MHF_Pig");
    defaultBeheading.add("minecraft:zombie_pigman:MHF_PigZombie");
    defaultBeheading.add("minecraft:sheep:MHF_Sheep");
    defaultBeheading.add("minecraft:slime:MHF_Slime");
    defaultBeheading.add("minecraft:spider:MHF_Spider");
    defaultBeheading.add("minecraft:squid:MHF_Squid");
    defaultBeheading.add("minecraft:villager:MHF_Villager");
    defaultBeheading.add("minecraft:witch:MHF_Witch");
    defaultBeheading.add("minecraft:wolf:MHF_Wolf");
    defaultBeheading.add("minecraft:guardian:MHF_Guardian");
    defaultBeheading.add("minecraft:elder_guardian:MHF_Guardian");
    defaultBeheading.add("minecraft:snow_golem:MHF_SnowGolem");
    defaultBeheading.add("minecraft:silverfish:MHF_Silverfish");
    defaultBeheading.add("minecraft:endermite:MHF_Endermite");
  }

  static {
    initConfig();
  }

  private static void initConfig() {
    buildDefaultHeadList();
    CFG.comment("Feature toggles; each of these will disable the registration of some content (items/enchants)").push(ModCyclic.MODID);
    String category = "";
    CABLES = CFG.comment("Disable the cables aka pipes").define(category + "cables", true);
    BOOMERANGS = CFG.comment("Disable the 3 boomerang items").define(category + "boomerangs", true);
    SPIKES = CFG.comment("Disable the 3 spike blocks").define(category + "spikes", true);
    SCAFFOLD = CFG.comment("Disable the 3 scaffolding blocks").define(category + "scaffolding", true);
    CARROTS = CFG.comment("Disable the 5 carrot items that upgrade horses").define(category + "carrots", true);
    HEARTS = CFG.comment("Disable 2 heart items that increase and decrease max health").define(category + "hearts", true);
    GLOVE = CFG.comment("Disable the completely balanced climbing glove").define(category + "glove", true);
    CHARMS = CFG.comment("Disable 6 charms (four basic, the ultimate, and the anti-gravity)").define(category + "charms", true);
    EMERALD = CFG.comment("Disable 9 items, emerald armor and tools").define(category + "emeraldGear", true);
    SANDSTONE = CFG.comment("Disable 5 sandstone tools").define(category + "sandstoneGear", true);
    GEMGEAR = CFG.comment("Disable the endgame gear").define(category + "gemObsidianGear", true);
    NETHERBRICK = CFG.comment("Disable 5 netherbrick tools").define(category + "netherbrickGear", true);
    ENCHANTMENTS = CFG.comment("Disable all 11 enchantments").define(category + "enchantments", true);
    category = "energy.fuel.";
    PEATPOWER = CFG.comment("Power to repair one tick of durability")
        .defineInRange(category + "peat_fuel", 256, 1, 64000);
    PEATERICHPOWER = CFG.comment("Power gained burning one of this")
        .defineInRange(category + "peat_fuel_enriched", 256 * 4, 1, 64000);
    category = "energy.cost.";
    DISENCHANTERPOWER = CFG.comment("Power gained burning one of this").defineInRange(category + "disenchanter", 1500, 1, 64000);
    ANVILPOWER = CFG.comment("Power gained burning one of this").defineInRange(category + "anvil", 250, 1, 64000);
    MELTERPOWER = CFG.comment("Power per recipe").defineInRange(category + "melter", 5000, 1, 64000);
    SOLIDIFIERPOWER = CFG.comment("Power per recipe").defineInRange(category + "solidifier", 5000, 1, 64000);
    DROPPERPOWER = CFG.comment("Power per use").defineInRange(category + "dropper", 50, 1, 64000);
    FORESTERPOWER = CFG.comment("Power per use").defineInRange(category + "forester", 50, 1, 64000);
    HARVESTERPOWER = CFG.comment("Power per use").defineInRange(category + "harvester", 250, 1, 64000);
    category = "peat.";
    PEATCHANCE = CFG.comment("Chance that Peat Bog converts to Peat when wet (is multiplied by the number of surrounding water blocks)")
        .defineInRange(category + " conversionChance",
            0.08000000000000F,
            0.0010000000000F, 1F);
    category = "command.";
    COMMANDGETHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "gethome", false);
    COMMANDGETHELP = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "help", false);
    COMMANDHEALTH = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "health", true);
    COMMANDHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "home", true);
    COMMANDHUNGER = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "hunger", true);
    COMMANDNBT = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "nbtprint", false);
    COMMANDPINGNETHER = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "pingnether", false);
    COMMANDWORLDSPAWN = CFG.comment("True means only players with OP can use this /cyclic command").define(category + "worldspawn", true);
    //enchant subclasses
    category = "enchant.beheading.";
    BEHEADING_SKINS = CFG.comment("Beheading enchant add player skin head drop, add any mob id and any skin").define(category + "BeheadingEntityMHF", defaultBeheading);
    // done
    CFG.pop();
    COMMON_CONFIG = CFG.build();
  }

  public static void setup(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public static Map<String, String> getMappedBeheading() {
    Map<String, String> mappedBeheading = new HashMap<String, String>();
    for (String s : BEHEADING_SKINS.get()) {
      try {
        String[] stuff = s.split(":");
        String entity = stuff[0] + ":" + stuff[1];
        String skin = stuff[2];
        mappedBeheading.put(entity, skin);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Invalid config entry " + s);
      }
    }
    return mappedBeheading;
  }
}
