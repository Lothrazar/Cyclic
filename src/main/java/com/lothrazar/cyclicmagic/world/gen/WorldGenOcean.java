package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenOcean implements IWorldGenerator {
  private static int clayChance;
  private static int clayNumBlocks;
  private static int dirtChance;
  private static int dirtNumBlocks;
  private static int sandChance;
  private static int sandNumBlocks;
  // Thanks to ref :
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding/world-generation/
  private WorldGenerator genClay;
  private WorldGenerator genSand;
  private WorldGenerator genDirt;
  private static int MIN_HEIGHT;
  private static int MAX_HEIGHT;
  public WorldGenOcean() {
    this.genClay = new WorldGenMinable(Blocks.CLAY.getDefaultState(), clayNumBlocks, BlockMatcher.forBlock(Blocks.GRAVEL));
    this.genSand = new WorldGenMinable(Blocks.DIRT.getDefaultState(), dirtNumBlocks, BlockMatcher.forBlock(Blocks.GRAVEL));
    this.genDirt = new WorldGenMinable(Blocks.SAND.getDefaultState(), sandNumBlocks, BlockMatcher.forBlock(Blocks.GRAVEL));
  }
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if (world.provider.getDimension() == Const.Dimension.overworld
        && MIN_HEIGHT < MAX_HEIGHT) {
      if (clayChance > 0 && clayNumBlocks > 0)
        this.run(this.genClay, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, clayChance, MIN_HEIGHT, MAX_HEIGHT);
      if (sandChance > 0 && sandNumBlocks > 0)
        this.run(this.genSand, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, sandChance, MIN_HEIGHT, MAX_HEIGHT);
      if (dirtChance > 0 && dirtNumBlocks > 0)
        this.run(this.genDirt, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, dirtChance, MIN_HEIGHT, MAX_HEIGHT);
    }
  }
  private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
    if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
      throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
    int heightDiff = maxHeight - minHeight;
    BlockPos pos;
    Biome biome;
    for (int i = 0; i < chancesToSpawn; i++) {
      int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
      int y = minHeight + rand.nextInt(heightDiff);
      int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);
      pos = new BlockPos(x, y, z);
      biome = world.getBiomeGenForCoords(pos);
      if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN) {
        generator.generate(world, rand, pos);
      }
    }
  }
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.worldGenOceans;
    Property prop;
    prop = config.get(category, "clayChance", 30, "Chances of a clay patch.", 0, 90);
    prop.setRequiresMcRestart(true);
    clayChance = prop.getInt();
    prop = config.get(category, "dirtChance", 30, "Chances of a dirt patch.", 0, 90);
    prop.setRequiresMcRestart(true);
    dirtChance = prop.getInt();
    prop = config.get(category, "sandChance", 45, "Chances of a sand patch.", 0, 90);
    prop.setRequiresMcRestart(true);
    sandChance = prop.getInt();
    prop = config.get(category, "clayChance", 50, "Approximate size of clay patch.", 0, 64);
    prop.setRequiresMcRestart(true);
    clayNumBlocks = prop.getInt();
    prop = config.get(category, "dirtSize", 40, "Approximate size of dirt patch.", 0, 64);
    prop.setRequiresMcRestart(true);
    dirtNumBlocks = prop.getInt();
    prop = config.get(category, "sandSize", 25, "Approximate size of a sand patch.", 0, 64);
    prop.setRequiresMcRestart(true);
    sandNumBlocks = prop.getInt();
    prop = config.get(category, "MinHeight", 20, "Lowest point this ocean biome generator to run.", 1, 255);
    prop.setRequiresMcRestart(true);
    MIN_HEIGHT = prop.getInt();
    prop = config.get(category, "MaxHeight", 255, "Highest point this ocean biome generator to run.", 1, 255);
    prop.setRequiresMcRestart(true);
    MAX_HEIGHT = prop.getInt();
  }
}
