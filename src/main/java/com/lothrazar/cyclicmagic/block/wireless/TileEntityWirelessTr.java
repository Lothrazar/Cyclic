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
package com.lothrazar.cyclicmagic.block.wireless;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityWirelessTr extends TileEntityBaseMachineInvo implements ITickable {

  private BlockPos targetPos = null;

  public TileEntityWirelessTr() {
    super(0);
    //.getServer().worldServers[id].loadedTileEntityList
    //List<TileEntity> list = this.world.getMinecraftServer().worlds[0].loadedTileEntityList;
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    //oldState.getBlock() instanceof BlockRedstoneClock &&
    return !(newSate.getBlock() instanceof BlockRedstoneWireless);// : oldState != newSate;
  }

  @Override
  public void update() {
    if (targetPos == null) {
      return;
    }
    IBlockState target = world.getBlockState(targetPos);
    if (world.getTileEntity(targetPos) instanceof TileEntityWirelessRec
        && target.getBlock() instanceof BlockRedstoneWireless) {
      boolean targetPowered = target.getValue(BlockRedstoneWireless.POWERED);
      //update target based on my state
      boolean isPowered = world.isBlockPowered(pos);
      if (targetPowered != isPowered) {
        world.setBlockState(targetPos, target.withProperty(BlockRedstoneWireless.POWERED, isPowered));
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (targetPos != null) {
      UtilNBT.setTagBlockPos(compound, targetPos);
    }
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    targetPos = UtilNBT.getTagBlockPos(compound);
    super.readFromNBT(compound);
  }

  public BlockPos getTargetPos() {
    return this.targetPos;
  }

  public void setTargetPos(BlockPos p) {
    this.targetPos = p;
  }
}
