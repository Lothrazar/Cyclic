package com.lothrazar.cyclic.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.TileAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.TileAnvilVoid;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.enderctrl.EnderShelfHelper;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.eye.TileEye;
import com.lothrazar.cyclic.block.eyetp.TileEyeTp;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.generatorfood.TileGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.TileGeneratorFuel;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.melter.TileMelter;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.packager.TilePackager;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import com.lothrazar.cyclic.block.soundrecord.BlockSoundRecorder;
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
import com.lothrazar.cyclic.item.EdibleFlightItem;
import com.lothrazar.cyclic.item.EdibleSpecItem;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.TeleporterWandItem;
import com.lothrazar.cyclic.item.bauble.AutoCaveTorchItem;
import com.lothrazar.cyclic.item.bauble.AutoTorchItem;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.item.transporter.TileTransporterEmptyItem;
import com.lothrazar.cyclic.registry.CommandRegistry.CyclicCommands;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigRegistry {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static final ForgeConfigSpec.Builder CFGC = new ForgeConfigSpec.Builder();
  // Defaults
  private static final List<String> BEHEADING = new ArrayList<>();
  private static final List<String> UNCRAFT_IGNORE_ITEMS = new ArrayList<>();
  private static final List<String> MBALL_IGNORE = new ArrayList<>();
  private static final List<String> UNCRAFT_RECIPE_IDS = new ArrayList<>();
  private static final List<String> TRANSPORTBAG = new ArrayList<>();
  private static final String WALL = "####################################################################################";
  private static ForgeConfigSpec COMMON_CONFIG;
  private static ForgeConfigSpec CLIENT_CONFIG;
  public static IntValue PEATERICHPOWER;
  public static IntValue PEATPOWER;
  public static DoubleValue PEATCHANCE;
  public static BooleanValue COMMANDDEV;
  public static BooleanValue COMMANDGETHOME;
  public static BooleanValue COMMANDHEALTH;
  public static BooleanValue COMMANDHOME;
  public static BooleanValue COMMANDHUNGER;
  public static BooleanValue COMMANDPING;
  public static BooleanValue LOGINFO;
  public static IntValue HEARTXPMINUS;
  private static ConfigValue<List<? extends String>> BEHEADING_SKINS;
  private static ConfigValue<List<? extends String>> MBALL_IGNORE_LIST;
  public static BooleanValue CYAN_GENERATES;
  public static IntValue CHARM_LUCK;
  public static DoubleValue CHARM_SPEED;
  public static DoubleValue CHARM_ATTACKSPEED;
  static {
    buildDefaults();
    initConfig();
  }

  private static void buildDefaults() {
    //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
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
    UNCRAFT_IGNORE_ITEMS.add("minecraft:elytra");
    UNCRAFT_IGNORE_ITEMS.add("minecraft:tipped_arrow");
    UNCRAFT_IGNORE_ITEMS.add("minecraft:magma_block");
    UNCRAFT_IGNORE_ITEMS.add("minecraft:stick");
    UNCRAFT_IGNORE_ITEMS.add("minecraft:*_dye"); //getting flowers etc feels bad 
    UNCRAFT_IGNORE_ITEMS.add("spectrite:spectrite_arrow");
    UNCRAFT_IGNORE_ITEMS.add("spectrite:spectrite_arrow_special");
    UNCRAFT_IGNORE_ITEMS.add("techreborn:uumatter");
    UNCRAFT_IGNORE_ITEMS.add("projecte:*");
    //
    UNCRAFT_RECIPE_IDS.add("botania:cobweb");
    UNCRAFT_RECIPE_IDS.add("minecraft:magma_cream");
    UNCRAFT_RECIPE_IDS.add("minecraft:beacon");
    UNCRAFT_RECIPE_IDS.add("minecraft:stick_from_bamboo_item");
    UNCRAFT_RECIPE_IDS.add("minecraft:netherite_ingot_from_netherite_block");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:essence*");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:farmland_till");
    UNCRAFT_RECIPE_IDS.add("refinedstorage:coloring_recipes*");
    UNCRAFT_RECIPE_IDS.add("forcecraft:transmutation*");
    //
    TRANSPORTBAG.addAll(Arrays.asList(
        //legacy
        "parabox:parabox", "extracells:fluidcrafter", "extracells:ecbaseblock", "extracells:fluidfiller",
        //entire mods
        "exnihilosequentia:*", "refinedstorage:*",
        //tconctruct fluid processing
        "tconstruct:seared_fuel_tank", "tconstruct:smeltery_controller", "tconstruct:seared_drain", "tconstruct:seared_fuel_gauge", "tconstruct:seared_ingot_tank", "tconstruct:seared_ingot_gauge", "tconstruct:seared_melter", "tconstruct:seared_heater",
        //drains and ducts
        "tconstruct:scorched_drain", "tconstruct:scorched_duct", "tconstruct:scorched_chute", "tconstruct:foundry_controller", "tconstruct:scorched_alloyer",
        //rftools batteries
        "rftoolspower:cell3", "rftoolspower:cell2", "rftoolspower:cell1", "rftoolspower:cell3", "rftoolspower:cell2", "rftoolspower:cell1"));
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
    EnchantLaunch.CFG = CFG.comment("Set false to disable Multi Jump enchantment").define(EnchantLaunch.ID, true);
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
    COMMANDGETHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.GETHOME.toString(), false);
    COMMANDHEALTH = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HEALTH.toString(), true);
    COMMANDHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HOME.toString(), true);
    COMMANDHUNGER = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HUNGER.toString(), true);
    COMMANDDEV = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.DEV.toString(), false);
    COMMANDPING = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.PING.toString(), false);
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
    TileGeneratorFuel.RF_PER_TICK = CFG.comment("RF energy per tick generated while burning furnace fuel in this machine.  Burn time in ticks is the same as furnace values, so 1 coal = 1600 ticks")
        .defineInRange("generator_fuel.rf_per_tick", 80, 1, 6400);
    TileGeneratorFood.RF_PER_TICK = CFG.comment("RF energy per tick generated while burning food in this machine")
        .defineInRange("generator_food.rf_per_tick", 60, 1, 6400);
    TileGeneratorFood.TICKS_PER_FOOD = CFG.comment("This [factor * (item.food + item.saturation) = ticks] results in the number of ticks food will burn at. IE Bread has (5 + 0.6) with factor 100, will burn for 560 ticks.")
        .defineInRange("generator_food.ticks_per_food", 100, 1, 6400);
    CFG.comment(WALL, "Energy cost for various machines, either per use of an action or per tick (twenty ticks per second).", WALL)
        .push("cost");
    TilePackager.POWERCONF = CFG.comment("Power per recipe in the packager").defineInRange("packager", 50, 0, 64000);
    TileDisenchant.POWERCONF = CFG.comment("Power per use disenchanter").defineInRange("disenchanter", 2500, 0, 64000);
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
    TilePotion.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("beacon", 0, 0, 64000);
    CFG.pop(); //cost
    CFG.pop(); //energy
    CFG.comment(WALL, "Fluid cost for various machines", WALL)
        .push("fluid");
    TileAnvilMagma.FLUIDCOST = CFG.comment("Cost of magma fluid per action").defineInRange("anvil_magma", 100, 1, 64000);
    TileDisenchant.FLUIDCOST = CFG.comment("Cost of (or payment for if negative) per enchanted book generated").defineInRange("disenchanter", 100, -1000, 16000);
    TileAnvilVoid.FLUIDPAY = CFG.comment("Payment per void action, if not zero").defineInRange("void_anvil", 25, 0, 16000);
    CFG.pop(); //fluid
    CFG.comment(WALL, " Item specific configs", WALL).push("items");
    OreProspector.RANGE = CFG.comment("Ore Prospector radius around player to search for ores").defineInRange("prospector.range", 32, 1, 99);
    CFG.comment(WALL, " Emerald gear settings", WALL).push("emerald");
    MaterialRegistry.EMERALD_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 3.0F, 0.1F, 99F);
    MaterialRegistry.EMERALD_DMG = CFG.comment("Weapon damage").defineInRange("damage", 4.5F, 0.1F, 99F);
    MaterialRegistry.EMERALD_BOOTS = CFG.comment("Damage Reduction").defineInRange("boots", 4, 1, 99);
    MaterialRegistry.EMERALD_HELM = CFG.comment("Damage Reduction").defineInRange("helm", 4, 1, 99);
    MaterialRegistry.EMERALD_CHEST = CFG.comment("Damage Reduction").defineInRange("chest", 9, 1, 99);
    MaterialRegistry.EMERALD_LEG = CFG.comment("Damage Reduction").defineInRange("leg", 7, 1, 99);
    CFG.pop();
    //
    // 
    CFG.comment(WALL, " Obsidian gear settings", WALL).push("obsidian");
    MaterialRegistry.OBS_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 6.0F, 0.1F, 99F);
    MaterialRegistry.OBS_DMG = CFG.comment("Weapon damage").defineInRange("damage", 10.5F, 0.1F, 99F);
    MaterialRegistry.OBS_BOOTS = CFG.comment("Damage Reduction").defineInRange("boots", 7, 1, 99);
    MaterialRegistry.OBS_HELM = CFG.comment("Damage Reduction").defineInRange("helm", 7, 1, 99);
    MaterialRegistry.OBS_CHEST = CFG.comment("Damage Reduction").defineInRange("chest", 11, 1, 99);
    MaterialRegistry.OBS_LEG = CFG.comment("Damage Reduction").defineInRange("leg", 10, 1, 99);
    CFG.pop();
    //
    CFG.comment(WALL, " Settings for varios charms (curios)", WALL).push("charms");
    AutoTorchItem.LIGHT_LEVEL = CFG.comment("Light level limit for placing torches").defineInRange("charm_torch.light_level", 9, 0, 15);
    CHARM_LUCK = CFG.comment("Boost given by item charm_luck").defineInRange("luck", 10, 0, 100);
    CHARM_SPEED = CFG.comment("Boost given by item charm_speed").defineInRange("speed", 0.5F, 0, 2F);
    CHARM_ATTACKSPEED = CFG.comment("Boost given by item charm_attackspeed").defineInRange("attackspeed", 0.5F, 0, 2F);
    CFG.comment(WALL, " Caving Torch Charm settings", WALL).push("caving_torch");
    AutoCaveTorchItem.LIGHT_LIMIT = CFG.comment("Light level at which to start placing down a torch").defineInRange("light_limit", 7, 0, 13);
    AutoCaveTorchItem.LIGHT_TARGET = CFG.comment(
        "Light level of the current block after placing down a torch. Must be greater than light_limit",
        "Higher values means torches will be placed closer to you. Lower values means torches will overlap less,",
        "but might result in small dark spots between torches").defineInRange("light_target", 10, 1, 14);
    AutoCaveTorchItem.PREFER_WALLS = CFG.comment("Whether to prioritise placing torches on walls").define("prefer_walls", true);
    AutoCaveTorchItem.PREFER_LEFT_WALL = CFG.comment("Which wall to place torches on when digging a 1-wide tunnel", "True means left, False means right").define("prefer_left_wall", false);
    CFG.pop();
    //
    CFG.pop(); // charms
    //
    //
    CFG.comment(WALL, " Edible chorus settings", WALL).push("chorus");
    EdibleFlightItem.TICKS = CFG.comment("Seconds of flight per chorus_flight").defineInRange("ticks", 20 * 60, 1, 20 * 1000);
    //
    EdibleSpecItem.TICKS = CFG.comment("Seconds of noClip per chorus_spectral").defineInRange("ticks", 20 * 30, 1, 20 * 1000);
    CFG.pop(); // chorus
    //
    MBALL_IGNORE_LIST = CFG.comment("Entity ids that cannot be picked up with the Monster all").defineList("monster_ball_ignore_list", MBALL_IGNORE, it -> it instanceof String);
    CFG.comment("Wand settings").push("teleport_wand");
    TeleporterWandItem.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 256, 8, 1024);
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
    CFG.comment("Ender shelf settings").push("sound");
    BlockSoundRecorder.RADIUS = CFG.comment("Sound Recorder - how far out does it listen to record sounds").defineInRange("radius", 8, 1, 64);
    CFG.pop();
    CFG.comment("Ender shelf settings").push("ender_shelf");
    EnderShelfItemHandler.BOOKS_PER_ROW = CFG.comment("Each shelf has five rows.  Set the number of books stored per row here").defineInRange("books_per_row", 64, 1, 64);
    EnderShelfHelper.MAX_DIST = CFG.comment("Controller Max distance to search (using manhattan distance)").defineInRange("controller_distance", 64, 1, 256);
    CFG.pop(); // ender_shelf*6
    CFG.comment("Sprinkler settings").push("sprinkler");
    TileSprinkler.WATERCOST = CFG.comment("Water consumption").defineInRange("water", 5, 0, 1000);
    TileSprinkler.TIMER_FULL = CFG.comment("Tick rate.  20 will fire one block per second").defineInRange("ticks", 20, 1, 20);
    CFG.pop(); // sprinkler
    CFG.comment("Ender Anchor settings").push("eye_teleport");
    TileEyeTp.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 128, 2, 256);
    TileEyeTp.HUNGER = CFG.comment("Hunger cost on teleport").defineInRange("hunger", 1, 0, 20);
    TileEyeTp.EXP = CFG.comment("Exp cost on teleport").defineInRange("exp", 0, 0, 500);
    TileEyeTp.FREQUENCY = CFG.comment("Tick delay between checks, faster checks can consume server resources (1 means check every tick; 20 means only check once per second)")
        .defineInRange("frequency", 5, 1, 20);
    CFG.pop(); // eye_teleport
    CFG.comment("Ender Trigger settings").push("eye_redstone");
    TileEye.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 32, 2, 256);
    TileEye.FREQUENCY = CFG.comment("Tick delay between checks, faster checks can consume server resources (1 means check every tick; 20 means only check once per second)")
        .defineInRange("frequency", 5, 1, 20);
    CFG.pop();
    //
    CFG.comment("Uncrafter settings").push("uncrafter");
    TileUncraft.IGNORE_NBT = CFG.comment("False will mean you cannot uncraft damaged repairable items. When searching for a recipe, does it ignore all NBT values (such as enchantments, RepairCost, Damage, etc).  "
        + "For example, if false it will not uncraft damaged or enchanted items")
        .define("nbt_ignored", false);
    TileUncraft.IGNORELIST = CFG.comment("ITEM IDS HERE.  Block ALL recipes that output this item, no matter which recipe they use. For example, if you add 'minecraft:stick' here, all recipes that craft into one or more sticks will be disabled (including two wooden planks).")
        .defineList("ignore_list", UNCRAFT_IGNORE_ITEMS, it -> it instanceof String);
    TileUncraft.IGNORELIST_RECIPES = CFG.comment("RECIPE IDS HERE.  Block these recipe ids from being reversed, but do not block all recipes for this output item")
        .defineList("ignore_recipes", UNCRAFT_RECIPE_IDS, it -> it instanceof String);
    TileUncraft.TIMER = CFG.comment("Ticks used for each uncraft").defineInRange("ticks", 60, 1, 9999);
    CFG.pop(); //uncrafter
    CFG.pop(); //blocks
    CFG.pop(); //ROOT
    COMMON_CONFIG = CFG.build();
    CFGC.comment(WALL, "Client-side properties", WALL)
        .push(ModCyclic.MODID);
    CFGC.comment(WALL, "Block Rendering properties.  Color MUST have one # symbol and then six spots after so #000000 up to #FFFFFF", WALL)
        .push("blocks");
    CFGC.push("colors");
    ClientConfigCyclic.COLLECTOR_ITEM = CFGC.comment("Specify hex color of preview mode.  default #444044").define("collector_item", "#444044");
    ClientConfigCyclic.COLLECTOR_FLUID = CFGC.comment("Specify hex color of preview mode.  default #444044").define("collector_fluid", "#444044");
    ClientConfigCyclic.DETECTOR_ENTITY = CFGC.comment("Specify hex color of preview mode.  default #00FF00").define("detector_entity", "#00FF00");
    ClientConfigCyclic.DETECTOR_ITEM = CFGC.comment("Specify hex color of preview mode.  default #00AA00").define("detector_item", "#00AA00");
    ClientConfigCyclic.PEAT_FARM = CFGC.comment("Specify hex color of preview mode.  default #404040").define("peat_farm", "#404040");
    ClientConfigCyclic.MINER = CFGC.comment("Specify hex color of preview mode.  default #0000AA").define("miner", "#0000AA");
    ClientConfigCyclic.DROPPER = CFGC.comment("Specify hex color of preview mode.  default #AA0011").define("dropper", "#AA0011");
    ClientConfigCyclic.FORESTER = CFGC.comment("Specify hex color of preview mode.  default #11BB00").define("forester", "#11BB00");
    ClientConfigCyclic.HARVESTER = CFGC.comment("Specify hex color of preview mode.  default #00EE00").define("harvester", "#00EE00");
    ClientConfigCyclic.STRUCTURE = CFGC.comment("Specify hex color of preview mode.  default #FF0000").define("structure", "#FF0000");
    CFGC.pop();
    CFGC.pop(); //end of blocks
    CFGC.comment(WALL, "Item Rendering properties.  Color MUST have one # symbol and then six spots after so #000000 up to #FFFFFF", WALL)
        .push("items");
    CFGC.push("colors");
    ClientConfigCyclic.LOCATION = CFGC.comment("Specify hex color of preview mode for the GPS data card.  default #0000FF").define("location", "#0000FF");
    ClientConfigCyclic.SHAPE_DATA = CFGC.comment("Specify hex color of preview mode.  default #FFC800").define("shape_data", "#FFC800"); // orange
    ClientConfigCyclic.RANDOMIZE_SCEPTER = CFGC.comment("Specify hex color of preview mode.  default #0000FF").define("randomize_scepter", "#00EE00");
    ClientConfigCyclic.OFFSET_SCEPTER = CFGC.comment("Specify hex color of preview mode.  default #0000FF").define("offset_scepter", "#00FF00");
    ClientConfigCyclic.REPLACE_SCEPTER = CFGC.comment("Specify hex color of preview mode.  default #0000FF").define("replace_scepter", "#FFFF00");
    ClientConfigCyclic.BUILD_SCEPTER = CFGC.comment("Specify hex color of preview mode.  default #0000FF").define("build_scepter", "#0000FF");
    CFGC.pop();
    CFGC.pop(); //end of items
    CFGC.pop();
    CLIENT_CONFIG = CFGC.build();
  }

  public static void setup() {
    final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(ModCyclic.MODID + ".toml"))
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public static void setupClient() {
    final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(ModCyclic.MODID + "-client.toml"))
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    CLIENT_CONFIG.setConfig(configData);
  }

  @SuppressWarnings("unchecked")
  public static List<String> getMagicNetList() {
    return (List<String>) MBALL_IGNORE_LIST.get();
  }

  public static Map<String, String> getMappedBeheading() {
    Map<String, String> mappedBeheading = new HashMap<>();
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
