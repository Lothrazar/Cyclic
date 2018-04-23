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
package com.lothrazar.cyclicmagic.core.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class UtilPlantable {

  public static ItemStack tryPlantSeed(World world, BlockPos posForPlant, ItemStack stack) {
    BlockPos posSoil = posForPlant.down();
    if (stack != null && stack.getItem() instanceof IPlantable) {
      IPlantable seed = (IPlantable) stack.getItem();
      IBlockState crop = seed.getPlant(world, posForPlant);
      if (crop != null) {
        // mimic exactly what onItemUse.onItemUse is doing
        IBlockState state = world.getBlockState(posSoil);
        boolean canSustainPlant = state.getBlock().canSustainPlant(state, world, posSoil, EnumFacing.UP, seed);
        if (canSustainPlant) {
          if (world.isAirBlock(posForPlant)) {
            world.setBlockState(posForPlant, crop);
            stack.shrink(1);
            return stack;
          }
          else {
            return stack;// ie, dont do super
          }
        }
      }
    }
    return null;
  }
}
