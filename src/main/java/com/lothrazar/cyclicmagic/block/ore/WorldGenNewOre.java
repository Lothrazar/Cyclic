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
package com.lothrazar.cyclicmagic.block.ore;

import java.util.Random;
import com.lothrazar.cyclicmagic.registry.module.WorldModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenNewOre implements IWorldGenerator {

  private static final int MIN_HEIGHT = 5;
  private static final int MAX_HEIGHT = 128;

  public WorldGenNewOre() {
    for (BlockDimensionOre ore : WorldModule.ores) {
      if (ore.config.isVanilla() == false && WorldModule.enableModCompatOres == false) {
        continue;//quick patch
      }
      if (ore.config.isVanilla()) {
        if (ore.config.getDimension() == Const.Dimension.nether && WorldModule.netherOreEnabled == false) {
          return;
        }
        if (ore.config.getDimension() == Const.Dimension.end && WorldModule.endOreEnabled == false) {
          return;
        }
      }
      if (ore.config.getBlockCount() > 0
          && ore.config.isRegistered()) {
        ore.config.setGen(new WorldGenMinable(
            ore.getDefaultState(),
            ore.config.getBlockCount(),
            BlockMatcher.forBlock(ore.config.getBlockToReplaceObject())));
      }
    }
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    for (BlockDimensionOre ore : WorldModule.ores) {
      if (ore.config.getGen() != null &&
          ore.config.isRegistered() &&
          ore.config.getSpawnChance() > 0 &&
          ore.config.getDimension() == world.provider.getDimension()) {
        //now go!
        this.run(ore.config.getGen(), world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE,
            ore.config.getSpawnChance(), MIN_HEIGHT, MAX_HEIGHT);
      }
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
