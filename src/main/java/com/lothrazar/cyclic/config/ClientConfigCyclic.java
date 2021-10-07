package com.lothrazar.cyclic.config;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.awt.Color;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfigCyclic {

  private static final Color DEFAULTC = Color.GRAY;
  public static ConfigValue<String> COLLECTOR_ITEM;
  public static ConfigValue<String> COLLECTOR_FLUID;
  public static ConfigValue<String> DETECTOR_ENTITY;
  public static ConfigValue<String> DETECTOR_ITEM;
  public static ConfigValue<String> DROPPER;
  public static ConfigValue<String> FORESTER;
  public static ConfigValue<String> HARVESTER;
  public static ConfigValue<String> MINER;
  public static ConfigValue<String> PEAT_FARM;
  public static ConfigValue<String> STRUCTURE;
  public static ConfigValue<String> LOCATION;
  public static ConfigValue<String> SHAPE_DATA;
  public static ConfigValue<String> RANDOMIZE_SCEPTER;
  public static ConfigValue<String> OFFSET_SCEPTER;
  public static ConfigValue<String> REPLACE_SCEPTER;
  public static ConfigValue<String> BUILD_SCEPTER;

  public static Color getColor(BlockEntity tile) {
    //passing in tile in case of data overrides in future
    // but client config makes sense right now
    if (tile.getType() == TileRegistry.COLLECTOR_ITEM) {
      return parseColor(COLLECTOR_ITEM.get());
    }
    else if (tile.getType() == TileRegistry.COLLECTOR_FLUID) {
      return parseColor(COLLECTOR_FLUID.get());
    }
    else if (tile.getType() == TileRegistry.DETECTOR_ENTITY) {
      return parseColor(DETECTOR_ENTITY.get());
    }
    else if (tile.getType() == TileRegistry.DETECTOR_ITEM) {
      return parseColor(DETECTOR_ITEM.get());
    }
    else if (tile.getType() == TileRegistry.DROPPER) {
      return parseColor(DROPPER.get());
    }
    else if (tile.getType() == TileRegistry.FORESTER) {
      return parseColor(FORESTER.get());
    }
    else if (tile.getType() == TileRegistry.HARVESTER) {
      return parseColor(HARVESTER.get());
    }
    else if (tile.getType() == TileRegistry.MINER) {
      return parseColor(MINER.get());
    }
    else if (tile.getType() == TileRegistry.PEAT_FARM) {
      return parseColor(PEAT_FARM.get());
    }
    else if (tile.getType() == TileRegistry.STRUCTURE) {
      return parseColor(STRUCTURE.get());
    }
    else {
      ModCyclic.LOGGER.info("Default color for tile " + tile);
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
    if (item.getItem() == ItemRegistry.location) {
      return parseColor(LOCATION.get());
    }
    else if (item.getItem() == ItemRegistry.shape_data) {
      return parseColor(SHAPE_DATA.get());
    }
    else if (item.getItem() == ItemRegistry.RANDOMIZE_SCEPTER.get()) {
      return parseColor(RANDOMIZE_SCEPTER.get());
    }
    else if (item.getItem() == ItemRegistry.OFFSET_SCEPTER.get()) {
      return parseColor(OFFSET_SCEPTER.get());
    }
    else if (item.getItem() == ItemRegistry.REPLACE_SCEPTER.get()) {
      return parseColor(REPLACE_SCEPTER.get());
    }
    else if (item.getItem() == ItemRegistry.BUILD_SCEPTER.get()) {
      return parseColor(BUILD_SCEPTER.get());
    }
    else {
      ModCyclic.LOGGER.info("Default color for item " + item.getItem());
      return DEFAULTC;
    }
  }
}
