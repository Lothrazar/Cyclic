package com.lothrazar.cyclic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

  private static List<String> defaultBeheading = new ArrayList<>();
  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static BooleanValue SCAFFOLD;
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
  public static IntValue PEATERICHPOWER;
  public static IntValue PEATPOWER;
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
  public static BooleanValue LOGINFO;
  public static IntValue HEARTXPMINUS;

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
    final String WALL = "####################################################################################";
    final String WALLSM = "################"; //    CFG.translation(translationKey)
    CFG.comment(WALL,
        " Feature toggles; each of these will disable the registration of some content (items/enchants)", WALL)
        .push(ModCyclic.MODID);
    //
    //
    CABLES = CFG.comment("Disable the cables aka pipes").define("cables", true);
    BOOMERANGS = CFG.comment("Disable the 3 boomerang items").define("boomerangs", true);
    SPIKES = CFG.comment("Disable the 3 spike blocks").define("spikes", true);
    SCAFFOLD = CFG.comment("Disable the 3 scaffolding blocks").define("scaffolding", true);
    CARROTS = CFG.comment("Disable the 5 carrot items that upgrade horses").define("carrots", true);
    HEARTS = CFG.comment("Disable 2 heart items that increase and decrease max health").define("hearts", true);
    GLOVE = CFG.comment("Disable the completely balanced climbing glove").define("glove", true);
    CHARMS = CFG.comment("Disable 6 charms (four basic, the ultimate, and the anti-gravity)").define("charms", true);
    EMERALD = CFG.comment("Disable 9 items, emerald armor and tools").define("emeraldGear", true);
    SANDSTONE = CFG.comment("Disable 5 sandstone tools").define("sandstoneGear", true);
    GEMGEAR = CFG.comment("Disable the endgame gear").define("gemObsidianGear", true);
    NETHERBRICK = CFG.comment("Disable 5 netherbrick tools").define("netherbrickGear", true);
    ENCHANTMENTS = CFG.comment("Disable all 11 enchantments").define("enchantments", true);
    //
    CFG.comment(WALL, " Edit the permissions of all commands added by the mod.  false means anyone can use, true means only OP players can use  ", WALL).push("command");
    COMMANDGETHOME = CFG.comment("True means only players with OP can use this /cyclic command").define("gethome", false);
    COMMANDGETHELP = CFG.comment("True means only players with OP can use this /cyclic command").define("help", false);
    COMMANDHEALTH = CFG.comment("True means only players with OP can use this /cyclic command").define("health", true);
    COMMANDHOME = CFG.comment("True means only players with OP can use this /cyclic command").define("home", true);
    COMMANDHUNGER = CFG.comment("True means only players with OP can use this /cyclic command").define("hunger", true);
    COMMANDNBT = CFG.comment("True means only players with OP can use this /cyclic command").define("nbtprint", false);
    COMMANDPINGNETHER = CFG.comment("True means only players with OP can use this /cyclic command").define("pingnether", false);
    COMMANDWORLDSPAWN = CFG.comment("True means only players with OP can use this /cyclic command").define("worldspawn", true);
    CFG.pop();//command
    CFG.comment(WALL, " Logging related configs", WALL).push("logging");
    LOGINFO = CFG.comment("Enable unblock info logs; very spammy").define("info", false);
    CFG.pop();//logging 
    CFG.comment(WALL, " Energy related configs for machines and items", WALL).push("energy");
    CFG.comment(" Fuel gained by consuming items").push("fuel");
    PEATPOWER = CFG.comment(" Power gained burning one of this")
        .defineInRange("peat_fuel", 256, 1, 64000);
    PEATERICHPOWER = CFG.comment("Power gained burning one of this")
        .defineInRange("peat_fuel_enriched", 256 * 4, 1, 64000);
    CFG.pop();//fuel
    CFG.comment("Energy cost for various machines, either per use of an action or per tick (twenty ticks per second).").push("cost");
    TileDisenchant.POWERCONF = CFG.comment(WALLSM, "Power per use disenchanter").defineInRange("disenchanter", 1500, 0, 64000);
    TileAnvilAuto.POWERCONF = CFG.comment("Power per repair anvil").defineInRange("anvil", 250, 0, 64000);
    TileMelter.POWERCONF = CFG.comment("Power per recipe melter").defineInRange("melter", 5000, 0, 64000);
    TileSolidifier.POWERCONF = CFG.comment("Power per recipe solidifier").defineInRange("solidifier", 5000, 0, 64000);
    TileDropper.POWERCONF = CFG.comment("Power per use dropper").defineInRange("dropper", 50, 0, 64000);
    TileForester.POWERCONF = CFG.comment("Power per use forester").defineInRange("forester", 50, 0, 64000);
    TileHarvester.POWERCONF = CFG.comment("Power per use harvester").defineInRange("harvester", 250, 0, 64000);
    TilePotion.POWERCONF = CFG.comment("Power per tick beacon").defineInRange("beacon", 10, 0, 64000);
    TileMiner.POWERCONF = CFG.comment("Power per use miner").defineInRange("miner", 10, 0, 64000);
    TileUncraft.POWERCONF = CFG.comment("Power per use uncraft").defineInRange("uncraft", 1000, 0, 64000);
    TileFluidCollect.POWERCONF = CFG.comment("Power per use collector_fluid").defineInRange("collector_fluid", 500, 0, 64000);
    TilePeatFarm.POWERCONF = CFG.comment("Power per use peat_farm").defineInRange("peat_farm", 500, 0, 64000);
    TileCrafter.POWERCONF = CFG.comment("Power per use crafter").defineInRange("crafter", 500, 0, 64000);
    CFG.pop();//cost
    CFG.pop();//energy
    CFG.comment(WALL, " Item specific configs", WALL).push("items");
    CFG.comment("Peat blocks").push("peat");
    PEATCHANCE = CFG.comment("Chance that Peat Bog converts to Peat when wet (is multiplied by the number of surrounding water blocks)")
        .defineInRange("conversionChance",
            0.08000000000000F,
            0.0010000000000F, 1F);
    CFG.pop();//peat
    CFG.comment("Heart items").push("heart");
    HEARTXPMINUS = CFG.comment("Experience given when eating a poisoned heart")
        .defineInRange("experience",
            500,
            0, 99999);
    CFG.pop();//heart
    CFG.pop();//items
    CFG.comment(WALL, " Enchantment related configs", WALL).push("enchant");
    BEHEADING_SKINS = CFG.comment("Beheading enchant add player skin head drop, add any mob id and any skin").define("beheading."
        + "BeheadingEntityMHF", defaultBeheading);
    CFG.pop();//enchant
    //
    //
    //
    //
    CFG.pop();//ROOT
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
        ModCyclic.LOGGER.error("Beheading Enchantment: Invalid config entry " + s);
      }
    }
    return mappedBeheading;
  }
}
