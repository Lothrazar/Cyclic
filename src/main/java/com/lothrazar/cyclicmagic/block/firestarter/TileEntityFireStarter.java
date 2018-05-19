package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityFireStarter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  private int needsRedstone = 1;
  private int hOffset = 0;
  private int delay = 11;

  public TileEntityFireStarter() {
    super(1);
    timer = delay;
    this.setSlotsForInsert(0);
  }

  public static enum Fields {
    TIMER, REDSTONE, DELAY, OFFSET;
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      BlockPos target = this.getCurrentFacingPos().offset(this.getCurrentFacing(), hOffset);
      if (world.isAirBlock(target)) {
        world.setBlockState(target, Blocks.FIRE.getDefaultState());
        timer = delay;
      }
    }
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case DELAY:
        return delay;
      case OFFSET:
        return hOffset;
      default:
      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case DELAY:
        delay = value;
      break;
      case OFFSET:
        hOffset = value;
      default:
      break;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    this.delay = tagCompound.getInteger("delay");
    this.hOffset = tagCompound.getInteger("hOffset");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger("delay", this.delay);
    tagCompound.setInteger("hOffset", this.hOffset);
    return super.writeToNBT(tagCompound);
  }
}
