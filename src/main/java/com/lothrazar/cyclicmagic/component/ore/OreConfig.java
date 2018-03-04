package com.lothrazar.cyclicmagic.component.ore;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class OreConfig {
  private int blockCount = 8;
  private int spawnChance = 10;
  private String blockCountConfig;
  private String spawnChanceConfig;
  private String configCategory;
  private String blockId;
  private boolean isRegistered = true;
  private int dimension;
  private WorldGenMinable gen = null;

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
}
