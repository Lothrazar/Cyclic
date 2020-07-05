package com.lothrazar.cyclic;

import java.nio.file.Path;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

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
  static {
    initConfig();
  }

  private static void initConfig() {
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
}
