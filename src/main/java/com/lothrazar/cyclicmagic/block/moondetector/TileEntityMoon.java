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
package com.lothrazar.cyclicmagic.block.moondetector;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityMoon extends TileEntityBaseMachineInvo implements ITickable {

  public static enum Fields {
    POWER;
  }

  private int power;

  public TileEntityMoon() {
    super(0);
    power = 0;
  }

  public int getPower() {
    return this.power;
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return !(newSate.getBlock() instanceof BlockMoonDetector);
  }

  @Override
  public void update() {
    //    
    if (this.isValid() && !world.isRemote && this.world.getTotalWorldTime() % 20L == 0L) {
      //https://minecraft.gamepedia.com/Moon
      int prevPowered = this.power;
      //[0, 7] inclusive, so bump that up
      power = world.provider.getMoonPhase(world.getWorldTime()) * 2 + 1;
      if (prevPowered != power) {
        //     ModCyclic.logger.log(" CHANGE TO " + power);
        //save my state 
        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockMoonDetector.POWER, power));
        //all other directions read update, but not down apparently!   
        world.notifyNeighborsOfStateChange(pos.down(), world.getBlockState(pos.down()).getBlock(), true);
      }
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case POWER:
        return power;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case POWER:
        power = MathHelper.clamp(value, 0, 16);
      break;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("power", power);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    power = compound.getInteger("power");
  }
}
