package com.lothrazar.cyclic.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.cyclic.CyclicLogger;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockNoTraders;
import com.lothrazar.cyclic.block.CandlePeaceBlock;
import com.lothrazar.cyclic.block.CandleWaterBlock;
import com.lothrazar.cyclic.block.LavaSpongeBlock;
import com.lothrazar.cyclic.block.PeatBlock;
import com.lothrazar.cyclic.block.antipotion.TileAntiBeacon;
import com.lothrazar.cyclic.block.anvil.TileAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.TileAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.TileAnvilVoid;
import com.lothrazar.cyclic.block.beaconpotion.TilePotion;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.TileCableFluid;
import com.lothrazar.cyclic.block.collectfluid.TileFluidCollect;
import com.lothrazar.cyclic.block.crafter.TileCrafter;
import com.lothrazar.cyclic.block.disenchant.TileDisenchant;
import com.lothrazar.cyclic.block.dropper.TileDropper;
import com.lothrazar.cyclic.block.enderctrl.EnderShelfHelper;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.eye.TileEye;
import com.lothrazar.cyclic.block.eyetp.TileEyeTp;
import com.lothrazar.cyclic.block.forester.TileForester;
import com.lothrazar.cyclic.block.generatorexpl.BlockDestruction;
import com.lothrazar.cyclic.block.generatorfood.TileGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.TileGeneratorFuel;
import com.lothrazar.cyclic.block.generatorsolar.BlockGeneratorSolar;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.magnet.BlockMagnetPanel;
import com.lothrazar.cyclic.block.miner.TileMiner;
import com.lothrazar.cyclic.block.packager.TilePackager;
import com.lothrazar.cyclic.block.peatfarm.TilePeatFarm;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.block.soundrecord.BlockSoundRecorder;
import com.lothrazar.cyclic.block.sprinkler.TileSprinkler;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.block.tp.BlockTeleport;
import com.lothrazar.cyclic.block.uncrafter.TileUncraft;
import com.lothrazar.cyclic.block.user.TileUser;
import com.lothrazar.cyclic.enchant.AutoSmeltEnchant;
import com.lothrazar.cyclic.enchant.BeekeeperEnchant;
import com.lothrazar.cyclic.enchant.BeheadingEnchant;
import com.lothrazar.cyclic.enchant.CurseEnchant;
import com.lothrazar.cyclic.enchant.DisarmEnchant;
import com.lothrazar.cyclic.enchant.ElytraLaunchEnchant;
import com.lothrazar.cyclic.enchant.ExcavationEnchant;
import com.lothrazar.cyclic.enchant.GrowthEnchant;
import com.lothrazar.cyclic.enchant.LifeLeechEnchant;
import com.lothrazar.cyclic.enchant.MagnetEnchant;
import com.lothrazar.cyclic.enchant.MultishotEnchant;
import com.lothrazar.cyclic.enchant.PearlEnchant;
import com.lothrazar.cyclic.enchant.QuickdrawEnchant;
import com.lothrazar.cyclic.enchant.ReachEnchant;
import com.lothrazar.cyclic.enchant.StandEnchant;
import com.lothrazar.cyclic.enchant.SteadyEnchant;
import com.lothrazar.cyclic.enchant.StepEnchant;
import com.lothrazar.cyclic.enchant.TravellerEnchant;
import com.lothrazar.cyclic.enchant.VenomEnchant;
import com.lothrazar.cyclic.enchant.XpEnchant;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.TeleporterWandItem;
import com.lothrazar.cyclic.item.bauble.AutoCaveTorchItem;
import com.lothrazar.cyclic.item.bauble.AutoTorchItem;
import com.lothrazar.cyclic.item.bauble.CharmBase;
import com.lothrazar.cyclic.item.ender.ItemProjectileDungeon;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.food.EdibleFlightItem;
import com.lothrazar.cyclic.item.food.EdibleSpecItem;
import com.lothrazar.cyclic.item.food.HeartItem;
import com.lothrazar.cyclic.item.food.HeartToxicItem;
import com.lothrazar.cyclic.item.rf.WandHypnoItem;
import com.lothrazar.cyclic.item.rf.WandMissileItem;
import com.lothrazar.cyclic.item.transporter.TileTransporterEmptyItem;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.lothrazar.cyclic.registry.CommandRegistry.CyclicCommands;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigRegistry {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static final ForgeConfigSpec.Builder CFGC = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  private static ForgeConfigSpec CLIENT_CONFIG;
  // Defaults
  private static final List<String> BEHEADING = new ArrayList<>();
  private static final List<String> UNCRAFT_IGNORE_ITEMS = new ArrayList<>();
  private static final List<String> MBALL_IGNORE = new ArrayList<>();
  private static final List<String> UNCRAFT_RECIPE_IDS = new ArrayList<>();
  private static final List<String> TRANSPORTBAG = new ArrayList<>();
  private static ConfigValue<List<? extends String>> BEHEADING_SKINS;
  private static ConfigValue<List<? extends String>> MBALL_IGNORE_LIST;
  private static final String WALL = "####################################################################################";
  public static BooleanValue GENERATE_FLOWERS;
  public static BooleanValue CYAN_PODZOL_LEGACY;
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
    //a
    UNCRAFT_RECIPE_IDS.add("botania:cobweb");
    UNCRAFT_RECIPE_IDS.add("minecraft:magma_cream");
    UNCRAFT_RECIPE_IDS.add("minecraft:beacon");
    UNCRAFT_RECIPE_IDS.add("minecraft:stick_from_bamboo_item");
    UNCRAFT_RECIPE_IDS.add("minecraft:netherite_ingot_from_netherite_block");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:essence*");
    UNCRAFT_RECIPE_IDS.add("mysticalagriculture:farmland_till");
    UNCRAFT_RECIPE_IDS.add("refinedstorage:coloring_recipes*");
    UNCRAFT_RECIPE_IDS.add("forcecraft:transmutation*");
    UNCRAFT_RECIPE_IDS.add("cyclic:fireball");
    UNCRAFT_RECIPE_IDS.add("cyclic:shapeless/spark");
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
    CFG.comment(WALL, " Enchantment related configs (if disabled, they may still show up as NBT on books and such but have functions disabled and are not obtainable in survival)", WALL)
        .push("enchantment");
    ////////////////////////////////////////////////////////////////// enchantment
    AutoSmeltEnchant.CFG = CFG.comment("Set false to disable enchantment").define(AutoSmeltEnchant.ID + ".enabled", true);
    BeekeeperEnchant.CFG = CFG.comment("Set false to disable enchantment").define(BeekeeperEnchant.ID + ".enabled", true);
    BeheadingEnchant.CFG = CFG.comment("Set false to disable enchantment").define(BeheadingEnchant.ID + ".enabled", true);
    BEHEADING_SKINS = CFG.comment("Beheading enchant add player skin head drop, add any mob id and any skin").defineList(BeheadingEnchant.ID + ".EntityMHF", BEHEADING,
        it -> it instanceof String);
    BeheadingEnchant.PERCDROP = CFG.comment("Base perecentage chance to drop a head on kill").defineInRange(BeheadingEnchant.ID + ".percent", 20, 1, 99);
    BeheadingEnchant.PERCPERLEVEL = CFG.comment("Percentage increase per level of enchant. Formula [percent + (level - 1) * per_level] ").defineInRange(BeheadingEnchant.ID + ".per_level", 25, 1, 99);
    CurseEnchant.CFG = CFG.comment("Set false to disable enchantment").define(CurseEnchant.ID + ".enabled", true);
    DisarmEnchant.CFG = CFG.comment("Set false to disable enchantment").define(DisarmEnchant.ID + ".enabled", true);
    ExcavationEnchant.CFG = CFG.comment("Set false to disable enchantment").define(ExcavationEnchant.ID + ".enabled", true);
    GrowthEnchant.CFG = CFG.comment("Set false to disable enchantment").define(GrowthEnchant.ID + ".enabled", true);
    ElytraLaunchEnchant.CFG = CFG.comment("Set false to disable Multi Jump enchantment").define(ElytraLaunchEnchant.ID + ".enabled", true);
    LifeLeechEnchant.CFG = CFG.comment("Set false to disable enchantment").define(LifeLeechEnchant.ID + ".enabled", true);
    MagnetEnchant.CFG = CFG.comment("Set false to disable enchantment").define(MagnetEnchant.ID + ".enabled", true);
    MultishotEnchant.CFG = CFG.comment("Set false to disable enchantment").define(MultishotEnchant.ID + ".enabled", true);
    PearlEnchant.CFG = CFG.comment("Set false to disable enchantment").define(PearlEnchant.ID + ".enabled", true);
    QuickdrawEnchant.CFG = CFG.comment("Set false to disable enchantment").define(QuickdrawEnchant.ID + ".enabled", true);
    ReachEnchant.CFG = CFG.comment("Set false to disable enchantment").define(ReachEnchant.ID + ".enabled", true);
    StepEnchant.CFG = CFG.comment("Set false to disable enchantment").define(StepEnchant.ID + ".enabled", true);
    SteadyEnchant.CFG = CFG.comment("Set false to disable enchantment").define(SteadyEnchant.ID + ".enabled", true);
    StandEnchant.CFG = CFG.comment("Set false to disable enchantment").define(StandEnchant.ID + ".enabled", true);
    StandEnchant.COST = CFG.comment("Base XP cost to activate at level 1 (level 2 is this/2)").defineInRange(StandEnchant.ID + ".xp_cost", 50, 1, 9999);
    StandEnchant.ABS = CFG.comment("How many ticks of Absorption hearts given on trigger, 0 to disable").defineInRange(StandEnchant.ID + ".potion_ticks", 600, 0, 9999);
    StandEnchant.COOLDOWN = CFG.comment("How many ticks of cooldown, 0 to disable").defineInRange(StandEnchant.ID + ".cooldown", 20, 0, 99999);
    TravellerEnchant.CFG = CFG.comment("Set false to disable enchantment").define(TravellerEnchant.ID + ".enabled", true);
    VenomEnchant.CFG = CFG.comment("Set false to disable enchantment").define(VenomEnchant.ID + ".enabled", true);
    XpEnchant.CFG = CFG.comment("Set false to disable enchantment").define(XpEnchant.ID + ".enabled", true);
    CFG.pop(); //enchantment
    CFG.comment(WALL, " Worldgen settings  ", WALL).push("worldgen"); //////////////////////////////////////////////////////////////////////////////////////////// worldgen
    GENERATE_FLOWERS = CFG.comment("Do the four generate in the world. "
        + " If false, the 4 flower blocks and 3 features (flower_all, flower_tulip_ flower_lime) will still be registered and can be used externally (data packs etc), "
        + "but the mod will not use the features to generate/place flowers in world-generation")
        .define("flowers.enabled", true);
    CYAN_PODZOL_LEGACY = CFG.comment("Enable the legacy feature that will spawn a Cyan flower when bonemeal is used on Podzol")
        .define("cyan_podzol_legacy.enabled", false);
    CFG.pop();
    CFG.comment(WALL, " Edit the permissions of all commands added by the mod.  false means anyone can use, true means only OP players can use  ", WALL)
        .push("command");
    CommandRegistry.COMMANDGETHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.GETHOME.toString(), false);
    CommandRegistry.COMMANDHEALTH = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HEALTH.toString(), true);
    CommandRegistry.COMMANDHOME = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HOME.toString(), true);
    CommandRegistry.COMMANDHUNGER = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.HUNGER.toString(), true);
    CommandRegistry.COMMANDDEV = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.DEV.toString(), false);
    CommandRegistry.COMMANDPING = CFG.comment("True means only players with OP can use this /cyclic command").define(CyclicCommands.PING.toString(), false);
    CFG.pop(); //command
    CFG.comment(WALL, " Logging related configs", WALL)
        .push("logging");
    CyclicLogger.LOGINFO = CFG.comment("Unblock info logs; very spammy; can be useful for testing certain issues").define("info", false);
    CFG.pop(); //logging 
    CFG.comment(WALL, " Item specific configs", WALL).push("items"); //////////////////////////////////////////////////////////////////////////////////////// items
    ShieldCyclicItem.LEATHER_PCT = CFG.comment("How much weaker than the regular shield is this item (used to calculate damage blocked)").defineInRange("shield_leather.blocked_damage_percent", 20, 0, 100);
    ShieldCyclicItem.WOOD_PCT = CFG.comment("How much weaker than the regular shield is this item (used to calculate damage blocked)").defineInRange("shield_wood.blocked_damage_percent", 60, 0, 100);
    ShieldCyclicItem.FLINT_PCT = CFG.comment("How much weaker than the regular shield is this item (used to calculate damage blocked)").defineInRange("shield_flint.blocked_damage_percent", 30, 0, 100);
    ShieldCyclicItem.FLINT_THORNS_PCT = CFG.comment("What % chance does this have to apply thorns damage against ranged non-explosive attackers").defineInRange("shield_flint.damage", 50, 0, 100);
    WandHypnoItem.COST = CFG.comment("Energy cost per item use").defineInRange("wand_hypno.energy", 500, 1, 50000);
    WandHypnoItem.RANGE = CFG.comment("Range to search out enemies for this multi-target attack").defineInRange("wand_hypno.range", 16, 1, 256);
    WandMissileItem.COST = CFG.comment("Energy cost per item use").defineInRange("wand_missile.energy", 150, 1, 50000);
    WandMissileItem.RANGE = CFG.comment("Range to search out enemies for this attack").defineInRange("wand_missile.range", 64, 1, 512);
    OreProspector.RANGE = CFG.comment("Ore Prospector radius around player to search for ores").defineInRange("prospector.range", 32, 1, 256);
    CFG.comment(WALL, " Emerald gear settings", WALL).push("emerald");
    MaterialRegistry.EMERALD_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 3.0F, 0.1F, 99F);
    MaterialRegistry.EMERALD_DMG = CFG.comment("Weapon damage").defineInRange("damage", 4.5F, 0.1F, 99F);
    MaterialRegistry.EMERALD_BOOTS = CFG.comment("Damage Reduction").defineInRange("boots", 4, 1, 99);
    MaterialRegistry.EMERALD_HELM = CFG.comment("Damage Reduction").defineInRange("helm", 4, 1, 99);
    MaterialRegistry.EMERALD_CHEST = CFG.comment("Damage Reduction").defineInRange("chest", 9, 1, 99);
    MaterialRegistry.EMERALD_LEG = CFG.comment("Damage Reduction").defineInRange("leg", 7, 1, 99);
    CFG.pop();
    CFG.comment(WALL, " Obsidian gear settings", WALL).push("obsidian");
    MaterialRegistry.OBS_TOUGH = CFG.comment("Armor toughness").defineInRange("toughness", 6.0F, 0.1F, 99F);
    MaterialRegistry.OBS_DMG = CFG.comment("Weapon damage").defineInRange("damage", 10.5F, 0.1F, 99F);
    MaterialRegistry.OBS_BOOTS = CFG.comment("Damage Reduction").defineInRange("boots", 7, 1, 99);
    MaterialRegistry.OBS_HELM = CFG.comment("Damage Reduction").defineInRange("helm", 7, 1, 99);
    MaterialRegistry.OBS_CHEST = CFG.comment("Damage Reduction").defineInRange("chest", 11, 1, 99);
    MaterialRegistry.OBS_LEG = CFG.comment("Damage Reduction").defineInRange("leg", 10, 1, 99);
    CFG.pop();
    ItemProjectileDungeon.RANGE = CFG.comment("Range in all directions to search for spawner").defineInRange("spawner_seeker.range", 64, 1, 256);
    AutoTorchItem.LIGHT_LEVEL = CFG.comment("Light level limit for placing torches").defineInRange("charm_torch.light_level", 9, 0, 15);
    CharmBase.CHARM_LUCK = CFG.comment("Boost given by item charm_luck").defineInRange("charm_luck.boost", 10, 0, 100);
    CharmBase.CHARM_SPEED = CFG.comment("Boost given by item charm_speed").defineInRange("charm_speed.boost", 0.5F, 0, 2F);
    CharmBase.CHARM_ATTACKSPEED = CFG.comment("Boost given by item charm_attackspeed").defineInRange("charm_attack_speed.boost", 0.5F, 0, 2F);
    CFG.comment(" Caving Torch Charm settings").push("caving_torch");
    AutoCaveTorchItem.LIGHT_LIMIT = CFG.comment("Light level at which to start placing down a torch").defineInRange("light_limit", 7, 0, 13);
    AutoCaveTorchItem.LIGHT_TARGET = CFG.comment(
        "Light level of the current block after placing down a torch. Must be greater than light_limit",
        "Higher values means torches will be placed closer to you. Lower values means torches will overlap less,",
        "but might result in small dark spots between torches").defineInRange("light_target", 10, 1, 14);
    AutoCaveTorchItem.PREFER_WALLS = CFG.comment("Whether to prioritise placing torches on walls").define("prefer_walls", true);
    AutoCaveTorchItem.PREFER_LEFT_WALL = CFG.comment("Which wall to place torches on when digging a 1-wide tunnel", "True means left, False means right").define("prefer_left_wall", false);
    CFG.pop(); // caving_torch 
    EdibleFlightItem.TICKS = CFG.comment("Seconds of flight per chorus_flight").defineInRange("chorus_flight.ticks", 20 * 60, 1, 20 * 1000);
    EdibleSpecItem.TICKS = CFG.comment("Seconds of noClip per chorus_spectral").defineInRange("chorus_spectral.ticks", 20 * 30, 1, 20 * 1000);
    MBALL_IGNORE_LIST = CFG.comment("Entity ids that cannot be picked up with the Monster all").defineList("monster_ball.ignore_list", MBALL_IGNORE, it -> it instanceof String);
    CFG.comment("Wand settings").push("teleport_wand");
    TeleporterWandItem.RANGE = CFG.comment("Maximum distance to activate").defineInRange("range", 256, 8, 1024);
    CFG.pop();
    CFG.comment("Sack of Holding settings").push("tile_transporter");
    TileTransporterEmptyItem.IGNORELIST = CFG.comment("Block these from being picked up")
        .defineList("disable_pickup", TRANSPORTBAG, it -> it instanceof String);
    CFG.pop();
    CFG.comment("Heart items").push("heart");
    HeartToxicItem.HEARTXPMINUS = CFG.comment("Experience given when eating a poisoned heart").defineInRange("experience", 500, 0, 99999);
    HeartItem.MAX = CFG.comment("Maximum number of hearts that can be attained (including initial 10)").defineInRange("maximum", 100, 1, 200);
    CFG.pop(); //heart
    CFG.pop(); //items
    CFG.comment(WALL, " Block specific configs", WALL).push("blocks"); //////////////////////////////////////////////////////////////////////////////////// blocks
    //buffer size for cables 
    TileAntiBeacon.RADIUS = CFG.comment("Radius to clear effects from living entities")
        .defineInRange("anti_beacon.radius", 16, 1, 128);
    TileAntiBeacon.TICKS = CFG.comment("Ticks to fire like a beacon and clear potion-effects from living entities")
        .defineInRange("anti_beacon.ticks", 30, 1, 9999);
    //TODO: config list of strings? or recipe or tags or something
    TileCableFluid.BUFFERSIZE = CFG.comment("How many buckets of buffer fluid the fluid cable can hold (for each direction. for example 2 here means 2000ub in each face)")
        .defineInRange("cables.fluid.buffer", 16, 1, 32);
    TileCableFluid.TRANSFER_RATE = CFG.comment("How many fluid units per tick can flow through these cables each tick (1 bucket = 1000) including normal flow and extraction mode")
        .defineInRange("cables.fluid.flow", 1000, 100, 32 * 1000);
    TileCableEnergy.BUFFERSIZE = CFG.comment("How much buffer the energy cables hold (must not be smaller than flow)")
        .defineInRange("cables.energy.buffer", 32000, 1, 32000 * 4);
    TileCableEnergy.TRANSFER_RATE = CFG.comment("How fast energy flows in these cables (must not be greater than buffer)")
        .defineInRange("cables.energy.flow", 1000, 100, 32 * 1000);
    //
    TileGeneratorFuel.RF_PER_TICK = CFG.comment("RF energy per tick generated while burning furnace fuel in this machine.  Burn time in ticks is the same as furnace values, so 1 coal = 1600 ticks")
        .defineInRange("generator_fuel.rf_per_tick", 80, 1, 6400);
    TileGeneratorFood.RF_PER_TICK = CFG.comment("RF energy per tick generated while burning food in this machine")
        .defineInRange("generator_food.rf_per_tick", 60, 1, 6400);
    TileGeneratorFood.TICKS_PER_FOOD = CFG.comment("This [factor * (item.food + item.saturation) = ticks] results in the number of ticks food will burn at. IE Bread has (5 + 0.6) with factor 100, will burn for 560 ticks.")
        .defineInRange("generator_food.ticks_per_food", 100, 1, 6400);
    BlockGeneratorSolar.ENERGY_GENERATE = CFG.comment("Base level of solar power generation (affected by weather contitions).")
        .defineInRange("generator_solar.energy", 4, 1, 100);
    BlockGeneratorSolar.TIMEOUT = CFG.comment("Ticks between power gen interval. Example: 40 ticks is 2 seconds. 0 means every tick it generates")
        .defineInRange("generator_solar.ticks", 60, 0, 6400);
    LavaSpongeBlock.RADIUS = CFG.comment("Reach of the sponge").defineInRange("sponge_lava.radius", 8, 1, 64);
    CandlePeaceBlock.HEIGHT = CFG.comment("Height reach of the candle for spawn prevention").defineInRange("peace_candle.height", 8, 1, 128);
    CandlePeaceBlock.RADIUS = CFG.comment("Reach of the candle for spawn prevention").defineInRange("peace_candle.radius", 32, 1, 128);
    BlockDestruction.HEIGHT = CFG.comment("Height for explosion prevention").defineInRange("altar_destruction.height", 8, 1, 128);
    BlockDestruction.RADIUS = CFG.comment("Reach for explosion prevention").defineInRange("altar_destruction.radius", 32, 1, 128);
    BlockMagnetPanel.RADIUS = CFG.comment("Reach for magnet distance to find items").defineInRange("magnet_block.radius", 16, 1, 128);
    BlockNoTraders.HEIGHT = CFG.comment("Height reach of the no_soliciting for spawn prevention").defineInRange("no_soliciting.height", 32, 1, 128);
    BlockNoTraders.RADIUS = CFG.comment("Reach of the no_soliciting for spawn prevention").defineInRange("no_soliciting.radius", 32, 1, 128);
    CandleWaterBlock.RADIUS = CFG.comment("Reach of the candle").defineInRange("water_candle.radius", 8, 1, 64);
    CandleWaterBlock.TICK_RATE = CFG.comment("Tick rate of the candle").defineInRange("water_candle.tick_rate", 60, 1, 2000);
    TilePackager.POWERCONF = CFG.comment("Power per recipe in the packager").defineInRange("packager.energy_cost", 50, 0, 64000);
    TileUser.POWERCONF = CFG.comment("Power per use user").defineInRange("user.energy_cost", 0, 0, 64000);
    TileAnvilAuto.POWERCONF = CFG.comment("Power per repair anvil").defineInRange("anvil.energy_cost", 250, 0, 64000);
    TileDropper.POWERCONF = CFG.comment("Power per use dropper").defineInRange("dropper.energy_cost", 50, 0, 64000);
    TileForester.POWERCONF = CFG.comment("Power per use forester").defineInRange("forester.energy_cost", 50, 0, 64000);
    TileHarvester.POWERCONF = CFG.comment("Power per use harvester").defineInRange("harvester.energy_cost", 250, 0, 64000);
    TilePotion.POWERCONF = CFG.comment("Power per tick beacon").defineInRange("beacon.energy_cost", 10, 0, 64000);
    TileMiner.POWERCONF = CFG.comment("Power per use miner").defineInRange("miner.energy_cost", 10, 0, 64000);
    TileUncraft.POWERCONF = CFG.comment("Power per use uncraft").defineInRange("uncraft.energy_cost", 1000, 0, 64000);
    TileFluidCollect.POWERCONF = CFG.comment("Power per use collector_fluid").defineInRange("collector_fluid.energy_cost", 500, 0, 64000);
    TilePeatFarm.POWERCONF = CFG.comment("Power per use peat_farm").defineInRange("peat_farm.energy_cost", 500, 0, 64000);
    TileCrafter.POWERCONF = CFG.comment("Power per use crafter").defineInRange("crafter.energy_cost", 500, 0, 64000);
    TileStructure.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("structure.energy_cost", 10, 0, 64000);
    BlockTeleport.POWERCONF = CFG.comment("Power per use").defineInRange("teleport.energy_cost", 400, 0, 64000);
    BlockTeleport.COSTDIM = CFG.comment("Power per use while crossing dimensions").defineInRange("teleport.energy_cost_xdim", 8000, 0, 64000);
    TilePotion.POWERCONF = CFG.comment("Power per tick while in use").defineInRange("beacon.energy_cost", 0, 0, 64000);
    PeatBlock.PEATCHANCE = CFG.comment("Chance that Peat Bog converts to Peat when wet (is multiplied by the number of surrounding water blocks)")
        .defineInRange("peat.conversion_chance",
            0.08000000000000F,
            0.0010000000000F, 1F);
    TileAnvilMagma.FLUIDCOST = CFG.comment("Cost of magma fluid per action").defineInRange("anvil_magma.fluid_cost", 100, 1, 64000);
    CFG.push("disenchanter");
    TileDisenchant.FLUIDCOST = CFG.comment("Cost of (or payment for if negative) per enchanted book generated").defineInRange("fluid_cost", 100, -1000, 16000);
    TileDisenchant.POWERCONF = CFG.comment("Power per use disenchanter").defineInRange("energy_cost", 2500, 0, 64000);
    CFG.pop();
    CFG.push("terra_preta");
    TileTerraPreta.TIMER_FULL = CFG.comment("Growth interval in ticks (100 would be every 5 seconds). Also affects terra glass").defineInRange("growth_interval", 100, 1, 64000);
    TileTerraPreta.CHANCE = CFG.comment("Chance that the crop will grow after the interval").defineInRange("growth_chance", 0.5, 0, 1);
    CFG.pop();
    CFG.push("anvil_void");
    TileAnvilVoid.FLUIDPAY = CFG.comment("Payment per void action, if not zero").defineInRange("fluid_cost", 25, 0, 16000);
    CFG.pop();
    CFG.push("sound");
    BlockSoundRecorder.RADIUS = CFG.comment("Sound Recorder - how far out does it listen to record sounds").defineInRange("radius", 8, 1, 64);
    CFG.pop();
    CFG.push("ender_shelf");
    EnderShelfItemHandler.BOOKS_PER_ROW = CFG.comment("Each shelf has five rows.  Set the number of books stored per row here").defineInRange("books_per_row", 64, 1, 64);
    EnderShelfHelper.MAX_DIST = CFG.comment("Controller Max distance to search (using manhattan distance)").defineInRange("controller_distance", 64, 1, 256);
    CFG.pop(); // ender_shelf*6
    CFG.push("sprinkler");
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
    CFG.push("uncrafter");
    TileUncraft.IGNORE_NBT = CFG.comment("When searching for a recipe, does it ignore all NBT values (such as enchantments, RepairCost, Damage, etc).  "
        + "For example, if false it will not uncraft damaged or enchanted items")
        .define("nbt_ignored", false);
    TileUncraft.IGNORELIST = CFG.comment("ITEM IDS HERE.  Block ALL recipes that output this item, no matter which recipe they use").defineList("ignore_list", UNCRAFT_IGNORE_ITEMS, it -> it instanceof String);
    TileUncraft.IGNORELIST_RECIPES = CFG.comment("RECIPE IDS HERE.  Block these recipe ids from being reversed, but do not block all recipes for this output item")
        .defineList("ignore_recipes", UNCRAFT_RECIPE_IDS, it -> it instanceof String);
    TileUncraft.TIMER = CFG.comment("Ticks used for each uncraft").defineInRange("ticks", 60, 1, 9999);
    CFG.pop(); //uncrafter
    CFG.pop(); //blocks
    CFG.pop(); //ROOT
    COMMON_CONFIG = CFG.build();
    initClientConfig();
  }

  private static void initClientConfig() {
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
    CFGC.push("text");
    ClientConfigCyclic.FLUID_BLOCK_STATUS = CFGC.comment("True means this will hide the fluid contents chat message (right click) on relevant blocks (pylon, fluid generator, fluid hopper, solidifier, sprinkler, tank, cask)").define("FluidContents", true);
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
