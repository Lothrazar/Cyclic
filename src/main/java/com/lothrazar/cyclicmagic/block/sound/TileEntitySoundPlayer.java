package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntitySoundPlayer extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  private int needsRedstone = 0;
  public static enum Fields {
    REDSTONE, TIMER
  }

  public TileEntitySoundPlayer() {
    super(1);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      //  UtilSound.playSound(world, pos, SoundEvents.AMBIENT_CAVE, SoundCategory.BLOCKS);
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
    }
    return -1;
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
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      default:
      break;
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
  }
}
