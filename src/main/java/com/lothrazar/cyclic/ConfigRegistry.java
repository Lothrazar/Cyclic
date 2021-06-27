package com.lothrazar.cyclic;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.eye.TileEye;
import com.lothrazar.cyclic.block.eyetp.TileEyeTp;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.sprinkler.TileSprinkler;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import com.lothrazar.cyclic.block.user.TileUser;
import com.lothrazar.cyclic.enchant.EnchantAutoSmelt;
import com.lothrazar.cyclic.enchant.EnchantBeekeeper;
import com.lothrazar.cyclic.enchant.EnchantBeheading;
import com.lothrazar.cyclic.enchant.EnchantCurse;
import com.lothrazar.cyclic.enchant.EnchantDisarm;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantGrowth;
import com.lothrazar.cyclic.enchant.EnchantLaunch;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantPearl;
import com.lothrazar.cyclic.enchant.EnchantQuickdraw;
import com.lothrazar.cyclic.enchant.EnchantReach;
import com.lothrazar.cyclic.enchant.EnchantStep;
import com.lothrazar.cyclic.enchant.EnchantTraveller;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import com.lothrazar.cyclic.item.TeleporterWandItem;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.item.transporter.TileTransporterEmptyItem;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigRegistry {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  // Defaults
  private static final List<String> BEHEADING = new ArrayList<>();
  private static final List<String> UNCRAFT = new ArrayList<>();
  private static final List<String> MBALL_IGNORE = new ArrayList<>();
  private static final List<String> UNCRAFT_RECIPE_IDS = new ArrayList<>();
  private static final List<String> TRANSPORTBAG = new ArrayList<>();
  private static final String WALL = "####################################################################################";
  private static ForgeConfigSpec COMMON_CONFIG;
  public static IntValue PEATERICHPOWER;
  public static IntValue PEATPOWER;
  public static DoubleValue PEATCHANCE;
  public static BooleanValue COMMANDNBT;
  public static BooleanValue COMMANDGETHOME;
  public static BooleanValue COMMANDHEALTH;
  public static BooleanValue COMMANDHOME;
  public static BooleanValue COMMANDHUNGER;
  public static BooleanValue COMMANDPINGNETHER;
  public static BooleanValue COMMANDGETHELP;
  public static BooleanValue LOGINFO;
  public static IntValue HEARTXPMINUS;
  private static ConfigValue<List<? extends String>> BEHEADING_SKINS;
  private static ConfigValue<List<? extends String>> MBALL_IGNORE_LIST;
  public static BooleanValue CYAN_GENERATES;
  static {
    buildDefaults();
    initConfig();
  }

  private static void buildDefaults() {
    //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
    //TODO config file for extra mod support 
    BEHEADING.add("minecraft:blaze:MHF_Blaze");
    BEHEADING.add("minecraft:cat:MHF_Ocelot");
    BEHEADING.add("minecraft:cave_spider:MHF_CaveSpider");
    BEHEADING.add("minecraft:chicken:MHF_Chicken");
    BEHEADING.add("minecraft:cow:MHF_Cow");
    BEHEADING.add("minecraft:enderman:MHF_Enderman");
    BEHEADING.add("minecraft:ghast:MHF_Ghast");
    BEHEADING.add("minecraft:iron_golem:MHF_Golem");
    BEHEADING.add("minecraft:magma_cube:MHF_LavaSlime");
    BEHEADING.add("minecraft:mooshroom:MHF_MushroomCow");
    BEHEADING.add("minecraft:ocelot:MHF_Ocelot");
    BEHEADING.add("minecraft:pig:MHF_Pig");
    BEHEADING.add("minecraft:zombie_pigman:MHF_PigZombie");
    BEHEADING.add("minecraft:sheep:MHF_Sheep");
    BEHEADING.add("minecraft:slime:MHF_Slime");
    BEHEADING.add("minecraft:spider:MHF_Spider");
    BEHEADING.add("minecraft:squid:MHF_Squid");
    BEHEADING.add("minecraft:villager:MHF_Villager");
    BEHEADING.add("minecraft:witch:MHF_Witch");
    BEHEADING.add("minecraft:wolf:MHF_Wolf");
    BEHEADING.add("minecraft:guardian:MHF_Guardian");
    BEHEADING.add("minecraft:elder_guardian:MHF_Guardian");
    BEHEADING.add("minecraft:snow_golem:MHF_SnowGolem");
    BEHEADING.add("minecraft:silverfish:MHF_Silverfish");
    BEHEADING.add("minecraft:endermite:MHF_Endermite");
    //
    //most of these are ported direct from 1.12 defaults, idk if these mods or items exist anymore
    UNCRAFT.add("minecraft:elytra");
    UNCRAFT.add("minecraft:tipped_arrow");
    UNCRAFT.add("minecraft:*_dye"); //getting flowers etc feels bad 
    UNCRAFT.add("spectrite:spectrite_arrow");
    UNCRAFT.add("spectrite:spectrite_arrow_special");
    UNCRAFT.add("techreborn:uumatter");
    UNCRAFT.add("projecte:*");
    //
    UNCRAFT_RECIPE_IDS.add("botania:cobweb");
    UNCRAFT_RECIPE_IDS.add("minecraft:magma_cream");
    UNCRAFT_RECIPE_IDS.add("minecraft:beacon");
    UNCRAFT_RECIPE_IDS.add("minecraft:stick_from_bamboo_item");
    UNCRAFT_RECIPE_IDS.add("minecraft:netherite_ingot_from_netherite_block");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:essence*");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:farmland_till");
    UNCRAFT_RECIPE_IDS.add("refinedstorage:coloring_recipes*");
    //
    TRANSPORTBAG.add("minecraft:spawner");
    TRANSPORTBAG.add("parabox:parabox");
    TRANSPORTBAG.add("extracells:fluidcrafter");
    TRANSPORTBAG.add("extracells:ecbaseblock");
    TRANSPORTBAG.add("extracells:fluidfiller");
    //
    MBALL_IGNORE.add("minecraft:ender_dragon");
    MBALL_IGNORE.add("minecraft:wither");
  }

  private static void initConfig() {
    CFG.comment(WALL, "Features with configurable properties are split into categories", WALL).push(ModCyclic.MODID);
    CFG.comment(WALL, " Enchantment related configs", WALL)
        .push("enchantment");
    EnchantAutoSmelt.CFG = CFG.comment("Set false to disable enchantment").define(EnchantAutoSmelt.ID, true);
    EnchantBeekeeper.CFG = CFG.comment("Set false to disable enchantment").define(EnchantBeekeeper.ID, true);
    EnchantBeheading.CFG = CFG.comment("Set false to disable enchantment").define(EnchantBeheading.ID, true);
    EnchantCurse.CFG = CFG.comment("Set false to disable enchantment").define(EnchantCurse.ID, true);
    EnchantDisarm.CFG = CFG.comment("Set false to disable enchantment").define(EnchantDisarm.ID, true);
    EnchantExcavation.CFG = CFG.comment("Set false to disable enchantment").define(EnchantExcavation.ID, true);
    EnchantGrowth.CFG = CFG.comment("Set false to disable enchantment").define(EnchantGrowth.ID, true);
    EnchantLaunch.CFG = CFG.comment("Set false to disable enchantment").define(EnchantLaunch.ID, true);
    EnchantLifeLeech.CFG = CFG.comment("Set false to disable enchantment").define(EnchantLifeLeech.ID, true);
    EnchantMagnet.CFG = CFG.comment("Set false to disable enchantment").define(EnchantMagnet.ID, true);
    EnchantMultishot.CFG = CFG.comment("Set false to disable enchantment").define(EnchantMultishot.ID, true);
    EnchantPearl.CFG = CFG.comment("Set false to disable enchantment").define(EnchantPearl.ID, true);
    EnchantQuickdraw.CFG = CFG.comment("Set false to disable enchantment").define(EnchantQuickdraw.ID, true);
    EnchantReach.CFG = CFG.comment("Set false to disable enchantment").define(EnchantReach.ID, true);
    EnchantStep.CFG = CFG.comment("Set false to disable enchantment").define(EnchantStep.ID, true);
    EnchantTraveller.CFG = CFG.comment("Set false to disable enchantment").define(EnchantTraveller.ID, true);
    EnchantVenom.CFG = CFG.comment("Set false to disable enchantment").define(EnchantVenom.ID, true);
    EnchantXp.CFG = CFG.comment("Set false to disable enchantment").define(EnchantXp.ID, true);
    BEHEADING_SKINS = CFG.comment("Beheading enchant add player skin head drop, add any mob id and any skin").defineList("beheadingEntityMHF", BEHEADING,
        it -> it instanceof String);
    CFG.pop(); //enchantment
    CFG.comment(WALL, " Worldgen settings  ", WALL)
        .push("worldgen");
    CYAN_GENERATES = CFG.comment("Does this generate in the world").define("flower_cyan", true);
    CFG.pop();
    CFG.comment(WALL, " Edit the permissions of all commands added by the mod.  false means anyone can use, true means only OP players can use  ", WALL)
        .push("command");
    COMMANDGETHOME = CFG.comment("True means only players with OP can use this /cyclic command").define("gethome", false);
    COMMANDGETHELP = CFG.comment("True means only players with OP can use this /cyclic command").define("help", false);
    COMMANDHEALTH = CFG.comment("True means only players with OP can use this /cyclic command").define("health", true);
    COMMANDHOME = CFG.comment("True means only players with OP can use this /cyclic command").define("home", true);
    COMMANDHUNGER = CFG.comment("True means only players with OP can use this /cyclic command").define("hunger", true);
    COMMANDNBT = CFG.comment("True means only players with OP can use this /cyclic command").define("nbtprint", false);
    COMMANDPINGNETHER = CFG.comment("True means only players with OP can use this /cyclic command").define("pingnether", false);
    CFG.pop(); //command
    CFG.comment(WALL, " Logging related configs", WALL)
        .push("logging");
    LOGINFO = CFG.comment("Unblock info logs; very spammy; can be useful for testing certain issues").define("info", false);
    CFG.pop(); //logging 
    CFG.comment(WALL, " Energy related configs for machines and items", WALL)
        .push("energy");
    CFG.comment(WALL, " Fuel gained by consuming items", WALL).push("fuel");
    PEATPOWER = CFG.comment(" Power gained burning one of this")
        .defineInRange("peat_fuel", 256, 1, 64000);
    PEATERICHPOWER = CFG.comment("Power gained burning one of this")
        .defineInRange("peat_fuel_enriched", 256 * 4, 1, 64000);
    CFG.pop(); //fuel
    CFG.comment(WALL, "Energy cost for various machines, either per use of an action or per tick (twenty ticks per second).", WALL)
        .push("cost");
    TileDisenchant.POWERCONF = CFG.comment("Power per use disenchanter").defineInRange("disenchanter", 1500, 0, 64000);
    TileUser.POWERCONF = CFG.comment("Power per use user").defineInRange("user", 50, 0, 64000);
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
    TileStructure.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("structure", 10, 0, 64000);
    //    TilePlacer.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("placer", 0, 0, 64000);
    //  TileBreaker.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("breaker", 0, 0, 64000);
    TilePotion.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("beacon", 0, 0, 64000);
    CFG.pop(); //cost
    CFG.pop(); //energy
    CFG.comment(WALL, " Item specific configs", WALL).push("items");
    CFG.comment(WALL, " Emerald gear settings", WALL).push("emerald");
    MaterialRegistry.EMERALD_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 3.0F, 0.1F, 99F);
    MaterialRegistry.EMERALD_DMG = CFG.comment("Weapon damage").defineInRange("damage", 4.5F, 0.1F, 99F);
    CFG.pop();
    //
    //CRYSTAL TOUGH 6.0
    //emerald TOUGH 3.0
    //
    // CRYSTAL DAMAGE 10.5
    // 
    //  EMERALD DAMAGE 4.5
    // 
    CFG.comment(WALL, " Obsidian gear settings", WALL).push("obsidian");
    MaterialRegistry.OBS_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 6.0F, 0.1F, 99F);
    MaterialRegistry.OBS_DMG = CFG.comment("Weapon damage").defineInRange("damage", 10.5F, 0.1F, 99F);
    CFG.pop();
    //
    //
    MBALL_IGNORE_LIST = CFG.comment("Entity ids that cannot be picked up with the Monster all").defineList("monster_ball_ignore_list", MBALL_IGNORE, it -> it instanceof String);
    CFG.comment("Wand settings").push("teleport_wand");
    TeleporterWandItem.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 128, 16, 256);
    CFG.pop();
    CFG.comment("Sack of Holding settings").push("tile_transporter");
    TileTransporterEmptyItem.IGNORELIST = CFG.comment("Block these from being picked up")
        .defineList("disable_pickup", TRANSPORTBAG, it -> it instanceof String);
    CFG.pop();
    CFG.comment("Peat blocks").push("peat");
    PEATCHANCE = CFG.comment("Chance that Peat Bog converts to Peat when wet (is multiplied by the number of surrounding water blocks)")
        .defineInRange("conversionChance",
            0.08000000000000F,
            0.0010000000000F, 1F);
    CFG.pop(); //peat
    CFG.comment("Heart items").push("heart");
    HEARTXPMINUS = CFG.comment("Experience given when eating a poisoned heart")
        .defineInRange("experience", 500, 0, 99999);
    HeartItem.MAX = CFG.comment("Maximum number of hearts that can be attained (including initial 10)")
        .defineInRange("maximum", 100, 1, 200);
    CFG.pop(); //heart
    CFG.pop(); //items
    CFG.comment(WALL, " Block specific configs", WALL)
        .push("blocks");
    CFG.comment("Sprinkler settings").push("sprinkler");
    TileSprinkler.WATERCOST = CFG.comment("Water consumption").defineInRange("water", 5, 0, 1000);
    TileSprinkler.TIMER_FULL = CFG.comment("Tick rate.  20 will fire one block per second").defineInRange("ticks", 20, 1, 20);
    CFG.pop();// sprinkler
    CFG.comment("Ender Anchor settings").push("eye_teleport");
    TileEyeTp.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 128, 2, 256);
    TileEyeTp.HUNGER = CFG.comment("Hunger cost on teleport").defineInRange("hunger", 1, 0, 20);
    TileEyeTp.EXP = CFG.comment("Exp cost on teleport").defineInRange("exp", 0, 0, 500);
    TileEyeTp.FREQUENCY = CFG.comment("Tick delay between checks, faster checks can consume server resources (1 means check every tick; 20 means only check once per second)")
        .defineInRange("frequency", 5, 1, 20);
    CFG.pop();// eye_teleport
    CFG.comment("Ender Trigger settings").push("eye_redstone");
    TileEye.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 32, 2, 256);
    TileEye.FREQUENCY = CFG.comment("Tick delay between checks, faster checks can consume server resources (1 means check every tick; 20 means only check once per second)")
        .defineInRange("frequency", 5, 1, 20);
    CFG.pop();
    //
    CFG.comment("Uncrafter settings").push("uncrafter");
    TileUncraft.IGNORE_NBT = CFG.comment("When searching for a recipe, does it ignore all NBT values (such as enchantments, RepairCost, Damage, etc).  "
        + "For example, if false it will not uncraft damaged or enchanted items")
        .define("nbt_ignored", true);
    TileUncraft.IGNORELIST = CFG.comment("ITEM IDS HERE.  Block ALL recipes that output this item, no matter which recipe they use").defineList("ignore_list", UNCRAFT, it -> it instanceof String);
    TileUncraft.IGNORELIST_RECIPES = CFG.comment("RECIPE IDS HERE.  Block these recipe ids from being reversed, but do not block all recipes for this output item")
        .defineList("ignore_recipes", UNCRAFT_RECIPE_IDS, it -> it instanceof String);
    TileUncraft.TIMER = CFG.comment("Ticks used for each uncraft").defineInRange("ticks", 60, 1, 9999);
    CFG.pop(); //uncrafter
    CFG.pop(); //blocks
    CFG.pop(); //ROOT
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

  @SuppressWarnings("unchecked")
  public static List<String> getMagicNetList() {
    return (List<String>) MBALL_IGNORE_LIST.get();
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
