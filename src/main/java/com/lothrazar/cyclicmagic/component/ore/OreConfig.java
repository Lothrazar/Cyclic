package com.lothrazar.cyclicmagic.component.ore;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class OreConfig {

  private int blockCount;
  private int spawnChance;
  private int blockCountDefault;
  private int spawnChanceDefault;
  private String blockCountConfig;
  private String spawnChanceConfig;
  private String configCategory;
  private String blockId;
  private boolean isRegistered = true;
  private boolean isRegisteredDefault = true;
  private int dimension;
  private int harvestLevel;
  private int harvestLevelDefault;
  private WorldGenMinable gen = null;
  private boolean isVanilla = false;
  private String blockToReplace;

  public int getBlockCount() {
    return blockCount;
  }

  public OreConfig setBlockCount(int blockCount) {
    this.blockCount = blockCount;
    return this;
  }

  public int getSpawnChance() {
    return spawnChance;
  }

  public OreConfig setSpawnChance(int spawnChance) {
    this.spawnChance = spawnChance;
    return this;
  }

  public boolean isRegistered() {
    return isRegistered;
  }

  public OreConfig setRegistered(boolean isRegistered) {
    this.isRegistered = isRegistered;
    return this;
  }

  public int getDimension() {
    return dimension;
  }

  public OreConfig setDimension(int dimension) {
    this.dimension = dimension;
    return this;
  }

  public String getBlockCountConfig() {
    return blockCountConfig;
  }

  public OreConfig setBlockCountConfig(String blockCountConfig) {
    this.blockCountConfig = blockCountConfig;
    return this;
  }

  public String getSpawnChanceConfig() {
    return spawnChanceConfig;
  }

  public OreConfig setSpawnChanceConfig(String spawnChanceConfig) {
    this.spawnChanceConfig = spawnChanceConfig;
    return this;
  }

  public String getConfigCategory() {
    return configCategory;
  }

  public OreConfig setConfigCategory(String configCategory) {
    this.configCategory = configCategory;
    return this;
  }

  public String getBlockToReplace() {
    return blockToReplace;
  }

  public Block getBlockToReplaceObject() {
    return Block.getBlockFromName(this.blockToReplace);
  }

  public OreConfig setBlockToReplace(String blockToReplace) {
    this.blockToReplace = blockToReplace;
    return this;
  }

  public WorldGenMinable getGen() {
    return gen;
  }

  public void setGen(WorldGenMinable gen) {
    this.gen = gen;
  }

  public String getBlockId() {
    return blockId;
  }

  public void setBlockId(String blockId) {
    this.blockId = blockId;
  }

  public int getBlockCountDefault() {
    return blockCountDefault;
  }

  public OreConfig setBlockCountDefault(int blockCountDefault) {
    this.blockCountDefault = blockCountDefault;
    return this;
  }

  public int getSpawnChanceDefault() {
    return spawnChanceDefault;
  }

  public OreConfig setSpawnChanceDefault(int spawnChanceDefault) {
    this.spawnChanceDefault = spawnChanceDefault;
    return this;
  }

  public boolean isRegisteredDefault() {
    return isRegisteredDefault;
  }

  public OreConfig setRegisteredDefault(boolean isRegisteredDefault) {
    this.isRegisteredDefault = isRegisteredDefault;
    return this;
  }

  public int getHarvestLevel() {
    return harvestLevel;
  }

  public void setHarvestLevel(int harvestLevel) {
    this.harvestLevel = harvestLevel;
  }

  public int getHarvestLevelDefault() {
    return harvestLevelDefault;
  }

  public OreConfig setHarvestLevelDefault(int harvestLevelDefault) {
    this.harvestLevelDefault = harvestLevelDefault;
    return this;
  }

  public boolean isVanilla() {
    return isVanilla;
  }

  public OreConfig setVanilla() {
    this.isVanilla = true;
    this.isRegisteredDefault = true;
    return this;
  }
}
