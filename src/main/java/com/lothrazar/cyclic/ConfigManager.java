package com.lothrazar.cyclic;

import java.nio.file.Path;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static BooleanValue EMERALD;
  public static BooleanValue SANDSTONE;
  public static BooleanValue GEMGEAR;
  public static BooleanValue NETHERBRICK;
  public static BooleanValue ENCHANTMENTS;
  public static IntValue ANVILPOWER;
  public static IntValue MELTERPOWER;
  public static IntValue SOLIDIFIERPOWER;
  public static DoubleValue PEATCHANCE;
  public static BooleanValue CABLES;
  public static IntValue PEATERICHPOWER;
  public static IntValue PEATPOWER;
  public static BooleanValue CHARMS;
  public static BooleanValue GLOVE;
  public static BooleanValue HEARTS;
  public static BooleanValue CARROTS;
  static {
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("Feature toggles; each of these will disable the registration of some content (items/blocks/enchants/etc)").push(ModCyclic.MODID);
    CARROTS = COMMON_BUILDER.comment("Disable the 5 carrot items that upgrade horses").define("carrots", true);
    HEARTS = COMMON_BUILDER.comment("Disable 2 heart items that increase and decrease max health").define("hearts", true);
    GLOVE = COMMON_BUILDER.comment("Disable the completely balanced climbing glove").define("glove", true);
    CHARMS = COMMON_BUILDER.comment("Disable 6 charms (four basic, the ultimate, and the anti-gravity)").define("charms", true);
    CABLES = COMMON_BUILDER.comment("Disable 4 total items; three cables and the cable wrench").define("cables", true);
    EMERALD = COMMON_BUILDER.comment("Disable 9 items, emerald armor and tools").define("emeraldGear", true);
    SANDSTONE = COMMON_BUILDER.comment("Disable 5 sandstone tools").define("sandstoneGear", true);
    GEMGEAR = COMMON_BUILDER.comment("Disable the endgame gear").define("gemObsidianGear", true);
    NETHERBRICK = COMMON_BUILDER.comment("Disable 5 netherbrick tools").define("netherbrickGear", true);
    ENCHANTMENTS = COMMON_BUILDER.comment("Disable all 11 enchantments").define("enchantments", true);
    PEATPOWER = COMMON_BUILDER.comment("Power to repair one tick of durability")
        .defineInRange("energy.fuel.peat_fuel", 256, 1, 64000);
    PEATERICHPOWER = COMMON_BUILDER.comment("Power gained burning one of this")
        .defineInRange("energy.fuel.peat_fuel_enriched", 256 * 4, 1, 64000);
    ANVILPOWER = COMMON_BUILDER.comment("Power gained burning one of this")
        .defineInRange("energy.cost.anvil", 250, 1, 64000);
    MELTERPOWER = COMMON_BUILDER.comment("Power per recipe")
        .defineInRange("energy.cost.melter", 5000, 1, 64000);
    SOLIDIFIERPOWER = COMMON_BUILDER.comment("Power per recipe")
        .defineInRange("energy.cost.solidifier", 5000, 1, 64000);
    PEATCHANCE = COMMON_BUILDER.comment("Chance that Peat Bog converts to Peat when wet (is multiplied by the number of surrounding water blocks)").defineInRange("peat.conversionChance",
        0.08000000000000F,
        0.0010000000000F, 1F);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public ConfigManager(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }
}
