package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenOreSingleton implements IWorldGenerator {
  private WorldGenMinable gen;
  private Block blockOre;
  private int minHeight = 0;
  public WorldGenOreSingleton(Block ore, int mh) {
    //http://minecraft.gamepedia.com/Ore#Availability
    // http://minecraft.gamepedia.com/Customized#Ore_settings
    blockOre = ore;
    minHeight = mh;
    gen = new WorldGenMinable(blockOre.getDefaultState(), 1, BlockMatcher.forBlock(Blocks.STONE));
  }
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    int spawnTries = 50;
    if (Const.Dimension.overworld == world.provider.getDimension()) {
      this.run(gen, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, spawnTries, minHeight, Const.WORLDHEIGHT - 1);
    }
  }
  private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
    if (minHeight < 0 || maxHeight > Const.WORLDHEIGHT || minHeight > maxHeight)
      throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
    int heightDiff = maxHeight - minHeight;
    BlockPos pos;
    for (int i = 0; i < chancesToSpawn; i++) {
      int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
      int y = minHeight + rand.nextInt(heightDiff);
      int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);
      pos = new BlockPos(x, y, z);
      generator.generate(world, rand, pos);
    }
  }
}
