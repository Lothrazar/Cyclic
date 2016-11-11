package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratePlant extends WorldGenerator {
  private BlockCrops crop;
  public GeneratePlant(BlockCrops state) {
    crop = state;
  }
  public boolean generate(World worldIn, Random rand, BlockPos pos) {
    //similar to WolrdGenPumpkin in vanilla code
    int fullGrownMeta = crop.getMaxAge();//func_185526_g();
    BlockPos blockpos;
    IBlockState soil;
    try {
      // for (int i = 0; i < 10; ++i) {//3 is fine, 10 is crash? yet World gen pumpkin can handle 64?
      blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
          rand.nextInt(8) - rand.nextInt(8));
      if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 255) {//
        soil = worldIn.getBlockState(blockpos.down());
        if (soil.getBlock() == Blocks.GRASS || soil.getBlock() == Blocks.DIRT) {
          worldIn.setBlockState(blockpos, this.crop.getStateFromMeta(MathHelper.getRandomIntegerInRange(rand, fullGrownMeta / 2 - 1, fullGrownMeta)), 2);
          worldIn.setBlockState(blockpos.down(), Blocks.FARMLAND.getDefaultState(), 2);
        }
        //}
      }
    }
    catch (StackOverflowError e) {
      /*
       * Description: Exception initializing level
       * 
       * java.lang.: Exception initializing level at
       * net.minecraft.world.gen.ChunkProviderServer.provideChunk(
       * ChunkProviderServer.java:153) at
       * net.minecraft.world.World.getChunkFromChunkCoords(World.java:349)
       */
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
    return true;
  }
}
