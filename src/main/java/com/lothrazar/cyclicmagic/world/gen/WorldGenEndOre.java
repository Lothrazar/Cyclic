package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.WorldModule;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenEndOre implements IWorldGenerator {
  private static final int MIN_HEIGHT = 5;
  private static final int MAX_HEIGHT = 128;
  private WorldGenMinable genRedstone;
  private WorldGenMinable genCoal;
  private WorldGenMinable genEmerald;
  private WorldGenMinable genLapis;
  private WorldGenMinable genDiamond;
  private WorldGenMinable genIron;
  private WorldGenMinable genGold;
  public static class Configs {
    public static int blockCountCoal = 8;
    public static int blockCountDiamond = 8;
    public static int blockCountEmerald = 8;
    public static int blockCountLapis = 8;
    public static int blockCountRedstone = 8;
    public static int blockCountIron = 8;
    public static int blockCountGold = 8;
    public static int spawnChanceCoal = 20;
    public static int spawnChanceDiamond = 10;
    public static int spawnChanceEmerald = 10;
    public static int spawnChanceLapis = 15;
    public static int spawnChanceRedstone = 18;
    public static int spawnChanceIron = 10;
    public static int spawnChanceGold = 20;
  }
  public WorldGenEndOre() {
    if (Configs.blockCountGold > 0)
      this.genGold = new WorldGenMinable(WorldModule.end_gold_ore.getDefaultState(), Configs.blockCountGold, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountIron > 0)
      this.genIron = new WorldGenMinable(WorldModule.end_iron_ore.getDefaultState(), Configs.blockCountIron, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountRedstone > 0)
      this.genRedstone = new WorldGenMinable(WorldModule.end_redstone_ore.getDefaultState(), Configs.blockCountRedstone, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountCoal > 0)
      this.genCoal = new WorldGenMinable(WorldModule.end_coal_ore.getDefaultState(), Configs.blockCountCoal, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountEmerald > 0)
      this.genEmerald = new WorldGenMinable(WorldModule.end_emerald_ore.getDefaultState(), Configs.blockCountEmerald, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountLapis > 0)
      this.genLapis = new WorldGenMinable(WorldModule.end_lapis_ore.getDefaultState(), Configs.blockCountLapis, BlockMatcher.forBlock(Blocks.END_STONE));
    if (Configs.blockCountDiamond > 0)
      this.genDiamond = new WorldGenMinable(WorldModule.end_diamond_ore.getDefaultState(), Configs.blockCountDiamond, BlockMatcher.forBlock(Blocks.END_STONE));
  }
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if (world.provider.getDimension() == Const.Dimension.end) {
      if (this.genGold != null && Configs.spawnChanceGold > 0)
        this.run(this.genGold, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceGold, MIN_HEIGHT, MAX_HEIGHT);
      if (this.genRedstone != null && Configs.spawnChanceRedstone > 0)
        this.run(this.genRedstone, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, Configs.spawnChanceRedstone, MIN_HEIGHT, MAX_HEIGHT);
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
      // if(biome == Biomes.sky){
      generator.generate(world, rand, pos);
      // }
    }
  }
}
