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
package com.lothrazar.cyclicmagic.block.conveyor;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConveyorAngle extends BlockConveyor implements IHasRecipe {

  private BlockConveyor drop;

  public BlockConveyorAngle(BlockConveyor drop) {
    super(drop.type);
    this.drop = drop;
    this.keepEntityGrounded = false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return Block.FULL_BLOCK_AABB;
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return true;
  }

  @Override
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    final double heightInc = 0.125D / 2;
    final double sideInc = heightInc;
    double edge = 1 - sideInc;
    double height = heightInc;
    switch (this.getFacingFromState(state)) {
      case DOWN:
      case UP:
      break;
      case WEST:
        while (edge > 0) {
          ModCyclic.logger.error("WEST " + edge + " at height " + height);
          addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, height, 0, edge, height + heightInc, 1));
          height += heightInc;
          edge -= sideInc;

        }
      break;
      case EAST:
        edge = sideInc;
        while (edge < 1) {

          addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(1, height, 0, edge, height + heightInc, 1));
          height += heightInc;
          edge += sideInc;
        }
      break;
      case NORTH:

        while (edge > 0) {

          addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, height, 0, 1, height + heightInc, edge));
          height += heightInc;
          edge -= sideInc;
        }
      break;
      case SOUTH:
        edge = sideInc;
        while (edge < 1) {

          addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, height, 1, 1, height + heightInc, edge));
          height += heightInc;
          edge += sideInc;
        }
      break;
    }
 
  }


  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this), new ItemStack(drop));
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(drop);
  }

}
