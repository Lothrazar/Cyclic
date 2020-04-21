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
  public static DoubleValue PEATCHANCE;
  public static BooleanValue CABLES;
  static {
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("Feature toggles").push(ModCyclic.MODID);
    CABLES = COMMON_BUILDER.comment("Disable these items").define("cables", true);
    EMERALD = COMMON_BUILDER.comment("Disable these items").define("emeraldGear", true);
    SANDSTONE = COMMON_BUILDER.comment("Disable these items").define("sandstoneGear", true);
    GEMGEAR = COMMON_BUILDER.comment("Disable these items").define("gemObsidianGear", true);
    NETHERBRICK = COMMON_BUILDER.comment("Disable these items").define("netherbrickGear", true);
    ENCHANTMENTS = COMMON_BUILDER.comment("Disable this feature").define("enchantments", true);
    ANVILPOWER = COMMON_BUILDER.comment("Power to repair one tick of durability").defineInRange("energy.anvil.cost", 250, 1, 64000);
    MELTERPOWER = COMMON_BUILDER.comment("Power cost per tick while it is running").defineInRange("energy.melter.cost", 100, 1, 64000);
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
