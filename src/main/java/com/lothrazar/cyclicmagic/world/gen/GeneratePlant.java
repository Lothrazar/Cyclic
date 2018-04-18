/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
      blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
      if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 255) {//
        soil = worldIn.getBlockState(blockpos.down());
        if (soil.getBlock() == Blocks.GRASS || soil.getBlock() == Blocks.DIRT) {
          worldIn.setBlockState(blockpos, this.crop.getStateFromMeta(MathHelper.getInt(rand, fullGrownMeta / 2 - 1, fullGrownMeta)), 2);
          worldIn.setBlockState(blockpos.down(), Blocks.FARMLAND.getDefaultState(), 2);
        }
        //}
      }
    }
    catch (StackOverflowError e) {
      /* Description: Exception initializing level
       * 
       * java.lang.: Exception initializing level at net.minecraft.world.gen.ChunkProviderServer.provideChunk( ChunkProviderServer.java:153) at
       * net.minecraft.world.World.getChunkFromChunkCoords(World.java:349) */
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
    return true;
  }
}
