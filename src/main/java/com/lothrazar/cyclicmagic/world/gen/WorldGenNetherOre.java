package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.module.WorldGenModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenNetherOre implements IWorldGenerator {
  private static final int MIN_HEIGHT = 5;
  private static final int MAX_HEIGHT = 128;
  private WorldGenerator genGold;
  private WorldGenerator genCoal;
  private WorldGenerator genEmerald;
  private WorldGenerator genLapis;
  private WorldGenerator genDiamond;
  private WorldGenMinable genIron;
  private WorldGenMinable genRedstone;
  public static class Configs {
    public static int blockCountCoal = 8;
    public static int blockCountDiamond = 8;
    public static int blockCountEmerald = 8;
    public static int blockCountLapis = 8;
    public static int blockCountGold = 8;
    public static int blockCountIron = 12;
    public static int spawnChanceCoal = 25;
    public static int spawnChanceDiamond = 6;
    public static int spawnChanceEmerald = 5;
    public static int spawnChanceGold = 45;
    public static int spawnChanceLapis = 10;
    public static int spawnChanceIron = 10;
    public static int spawnChanceRedstone = 20;
    public static int blockCountRedstone = 8;
  }
  public WorldGenNetherOre() {
    if (Configs.blockCountRedstone > 0)
      this.genRedstone = new WorldGenMinable(WorldGenModule.nether_redstone_ore.getDefaultState(), Configs.blockCountRedstone, BlockMatcher.forBlock(Blocks.NETHERRACK));
    
    
    if (Configs.blockCountIron > 0)
      this.genIron = new WorldGenMinable(WorldGenModule.nether_iron_ore.getDefaultState(), Configs.blockCountIron, BlockMatcher.forBlock(Blocks.NETHERRACK));
    if (Configs.blockCountGold > 0)
      this.genGold = new WorldGenMinable(WorldGenModule.nether_gold_ore.getDefaultState(), Configs.blockCountGold, BlockMatcher.forBlock(Blocks.NETHERRACK));
    if (Configs.blockCountCoal > 0)
      this.genCoal = new WorldGenMinable(WorldGenModule.nether_coal_ore.getDefaultState(), Configs.blockCountCoal, BlockMatcher.forBlock(Blocks.NETHERRACK));
    if (Configs.blockCountEmerald > 0)
      this.genEmerald = new WorldGenMinable(WorldGenModule.nether_emerald_ore.getDefaultState(), Configs.blockCountEmerald, BlockMatcher.forBlock(Blocks.NETHERRACK));
    if (Configs.blockCountLapis > 0)
      this.genLapis = new WorldGenMinable(WorldGenModule.nether_lapis_ore.getDefaultState(), Configs.blockCountLapis, BlockMatcher.forBlock(Blocks.NETHERRACK));
    if (Configs.blockCountDiamond > 0)
      this.genDiamond = new WorldGenMinable(WorldGenModule.nether_diamond_ore.getDefaultState(), Configs.blockCountDiamond, BlockMatcher.forBlock(Blocks.NETHERRACK));
  }
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if (world.provider.getDimension() == Const.Dimension.nether) {
      if (this.genRedstone != null && Configs.spawnChanceRedstone > 0)
        this.run(this.genRedstone, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceRedstone, MIN_HEIGHT, MAX_HEIGHT);
    
      
      if (this.genGold != null && Configs.spawnChanceGold > 0)
        this.run(this.genGold, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceGold, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genCoal != null && Configs.spawnChanceCoal > 0)
        this.run(this.genCoal, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceCoal, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genEmerald != null && Configs.spawnChanceEmerald > 0)
        this.run(this.genEmerald, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceEmerald, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genLapis != null && Configs.spawnChanceLapis > 0)
        this.run(this.genLapis, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceLapis, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genDiamond != null && Configs.spawnChanceDiamond > 0)
        this.run(this.genDiamond, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceDiamond, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genIron != null && Configs.spawnChanceIron > 0)
        this.run(this.genIron, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceIron, MIN_HEIGHT, MAX_HEIGHT);
    }
  }
  private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
    if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
      throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
    int heightDiff = maxHeight - minHeight;
    BlockPos pos;
    // BiomeGenBase biome;
    for (int i = 0; i < chancesToSpawn; i++) {
      int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
      int y = minHeight + rand.nextInt(heightDiff);
      int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);
      pos = new BlockPos(x, y, z);
      // biome = world.getBiomeGenForCoords(pos);
      // if(biome == Biomes.hell){// no longer do this, in case some mod adds biomes to nether
      generator.generate(world, rand, pos);
      // }
    }
  }
}
