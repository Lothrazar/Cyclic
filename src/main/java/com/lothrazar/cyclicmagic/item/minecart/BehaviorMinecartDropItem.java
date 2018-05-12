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
package com.lothrazar.cyclicmagic.item.minecart;

import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorMinecartDropItem implements IBehaviorDispenseItem {

  /**
   * Dispenses the specified ItemStack from a dispenser.
   */
  public final ItemStack dispense(IBlockSource source, ItemStack stack) {
    ItemStack itemstack = this.dispenseStack(source, stack);
    this.playDispenseSound(source);
    this.spawnDispenseParticles(source, getFacing(source));
    return itemstack;
  }

  public EnumFacing getFacing(IBlockSource state) {
    return getFacing(state.getBlockState());
  }

  public EnumFacing getFacing(IBlockState state) {
    if (state.getBlock() == Blocks.ACTIVATOR_RAIL) {
      EnumRailDirection dir = state.getValue(BlockRailPowered.SHAPE);
      switch (dir) {
        case ASCENDING_EAST:
          return EnumFacing.EAST;
        case ASCENDING_NORTH:
          return EnumFacing.NORTH;
        case ASCENDING_SOUTH:
          return EnumFacing.SOUTH;
        case ASCENDING_WEST:
          return EnumFacing.WEST;
        case EAST_WEST:
          return EnumFacing.NORTH;
        case NORTH_EAST:
          return EnumFacing.SOUTH;
        case NORTH_SOUTH:
          return EnumFacing.WEST;
        case NORTH_WEST:
          return EnumFacing.EAST;
        case SOUTH_EAST:
          return EnumFacing.NORTH;
        case SOUTH_WEST:
          return EnumFacing.EAST;
        default:
        break;
      }
    }
    return EnumFacing.NORTH;
  }

  /**
   * Dispense the specified stack, play the dispense sound and spawn particles.
   */
  protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
    EnumFacing enumfacing = getFacing(source);
    IPosition iposition = getDispensePosition(source);
    ItemStack itemstack = stack.splitStack(1);
    doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
    return stack;
  }

  /**
   * Get the position where the dispenser at the given Coordinates should dispense to.
   */
  public IPosition getDispensePosition(IBlockSource coords) {
    EnumFacing enumfacing = this.getFacing(coords.getBlockState());
    double d0 = coords.getX() + 0.7D * (double) enumfacing.getFrontOffsetX();
    double d1 = coords.getY() + 0.7D * (double) enumfacing.getFrontOffsetY();
    double d2 = coords.getZ() + 0.7D * (double) enumfacing.getFrontOffsetZ();
    return new PositionImpl(d0, d1, d2);
  }

  public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position) {
    double d0 = position.getX();
    double d1 = position.getY();
    double d2 = position.getZ();
    if (facing.getAxis() == EnumFacing.Axis.Y) {
      d1 = d1 - 0.125D;
    }
    else {
      d1 = d1 - 0.15625D;
    }
    EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
    double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
    entityitem.motionX = (double) facing.getFrontOffsetX() * d3;
    entityitem.motionY = 0.20000000298023224D;
    entityitem.motionZ = (double) facing.getFrontOffsetZ() * d3;
    entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
    entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
    entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
    worldIn.spawnEntity(entityitem);
  }

  /**
   * Play the dispense sound from the specified block.
   */
  protected void playDispenseSound(IBlockSource source) {
    source.getWorld().playEvent(1000, source.getBlockPos(), 0);
  }

  /**
   * Order clients to display dispense particles from the specified block and facing.
   */
  protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
    source.getWorld().playEvent(2000, source.getBlockPos(), this.getWorldEventDataFrom(facingIn));
  }

  private int getWorldEventDataFrom(EnumFacing facingIn) {
    return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
  }
}
