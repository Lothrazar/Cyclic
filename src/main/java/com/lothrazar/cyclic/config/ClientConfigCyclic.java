package com.lothrazar.cyclic.config;

import java.awt.Color;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfigCyclic {

  private static final Color DEFAULTC = Color.GRAY;
  public static ModConfigSpec.ConfigValue<String> COLLECTOR_ITEM;
  public static ModConfigSpec.ConfigValue<String> COLLECTOR_FLUID;
  public static ModConfigSpec.ConfigValue<String> DETECTOR_ENTITY;
  public static ModConfigSpec.ConfigValue<String> DETECTOR_ITEM;
  public static ModConfigSpec.ConfigValue<String> DROPPER;
  public static ModConfigSpec.ConfigValue<String> FORESTER;
  public static ModConfigSpec.ConfigValue<String> HARVESTER;
  public static ModConfigSpec.ConfigValue<String> MINER;
  public static ModConfigSpec.ConfigValue<String> PEAT_FARM;
  public static ModConfigSpec.ConfigValue<String> STRUCTURE;
  public static ModConfigSpec.ConfigValue<String> LASER_COLOR;
  public static ModConfigSpec.ConfigValue<String> LOCATION;
  public static ModConfigSpec.ConfigValue<String> SHAPE_DATA;
  public static ModConfigSpec.ConfigValue<String> SCEPTER_RANDOMIZE;
  public static ModConfigSpec.ConfigValue<String> SCEPTER_OFFSET;
  public static ModConfigSpec.ConfigValue<String> SCEPTER_REPLACE;
  public static ModConfigSpec.ConfigValue<String> SCEPTER_BUILD;
  public static ModConfigSpec.ConfigValue<Boolean> FLUID_BLOCK_STATUS;
  public static ModConfigSpec.ConfigValue<Boolean> ENERGY_HUD;
  public static ModConfigSpec.ConfigValue<Boolean> SCULK_FLUID_XP_SOUND;

  public static Color getColor(BlockEntity tile) {
    //passing in tile in case of data overrides in future
    // but client config makes sense right now
    if (tile.getType() == TileRegistry.COLLECTOR.get()) {
      return parseColor(COLLECTOR_ITEM.get());
    }
    else if (tile.getType() == TileRegistry.COLLECTOR_FLUID.get()) {
      return parseColor(COLLECTOR_FLUID.get());
    }
    else if (tile.getType() == TileRegistry.DETECTOR_ENTITY.get()) {
      return parseColor(DETECTOR_ENTITY.get());
    }
    else if (tile.getType() == TileRegistry.DETECTOR_ITEM.get()) {
      return parseColor(DETECTOR_ITEM.get());
    }
    else if (tile.getType() == TileRegistry.DROPPER.get()) {
      return parseColor(DROPPER.get());
    }
    else if (tile.getType() == TileRegistry.FORESTER.get()) {
      return parseColor(FORESTER.get());
    }
    else if (tile.getType() == TileRegistry.HARVESTER.get()) {
      return parseColor(HARVESTER.get());
    }
    else if (tile.getType() == TileRegistry.MINER.get()) {
      return parseColor(MINER.get());
    }
    else if (tile.getType() == TileRegistry.PEAT_FARM.get()) {
      return parseColor(PEAT_FARM.get());
    }
    else if (tile.getType() == TileRegistry.STRUCTURE.get()) {
      return parseColor(STRUCTURE.get());
    }
    else {
      ModCyclic.LOGGER.error("Default color for tile " + tile);
      return DEFAULTC;
    }
  }

  private static Color parseColor(String string) {
    //the reverse is 
    //   String h = String.format("#%02x%02x%02x", Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue());
    try {
      return Color.decode(string);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Invalid color string in client config, try default #404040" + string);
    }
    return DEFAULTC;
  }

  public static Color getColor(ItemStack item) {
    if (item.getItem() == ItemRegistry.LOCATION_DATA.get()) {
      return parseColor(LOCATION.get());
    }
    else if (item.getItem() == ItemRegistry.SHAPE_DATA.get()) {
      return parseColor(SHAPE_DATA.get());
    }
    else if (item.getItem() == ItemRegistry.SCEPTER_RANDOMIZE.get()) {
      return parseColor(SCEPTER_RANDOMIZE.get());
    }
    else if (item.getItem() == ItemRegistry.SCEPTER_OFFSET.get()) {
      return parseColor(SCEPTER_OFFSET.get());
    }
    else if (item.getItem() == ItemRegistry.SCEPTER_REPLACE.get()) {
      return parseColor(SCEPTER_REPLACE.get());
    }
    else if (item.getItem() == ItemRegistry.SCEPTER_BUILD.get()) {
      return parseColor(SCEPTER_BUILD.get());
    }
    else {
      ModCyclic.LOGGER.error("Default color for item " + item.getItem());
      return DEFAULTC;
    }
  }
}
