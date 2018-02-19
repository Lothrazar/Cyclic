package com.lothrazar.cyclicmagic.component.dropper;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityDropperExact extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  private int needsRedstone = 1;
  private int slotCurrent = 0;
  private int dropCount = 1;
  private int timerFull = 10;
  public static enum Fields {
    TIMER, REDSTONE;
  }
  public TileEntityDropperExact() {
    super(9);
    timer = timerFull;
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      ItemStack dropMe = this.getStackInSlot(slotCurrent).copy();
      if (dropMe.isEmpty() == false) {
        timer = timerFull;
        //    if (world.isRemote == false) {
        int amtDrop = Math.min(this.dropCount, dropMe.getCount());
        dropMe.setCount(amtDrop);
         UtilItemStack.dropItemStackMotionless(world, this.getCurrentFacingPos(), dropMe);
//        eItem.setVelocity(0, 0, 0);
//        eItem.motionX = eItem.motionY=eItem.motionZ=0;
//        eItem.hoverStart = 0;
        this.decrStackSize(slotCurrent, amtDrop);
        //  }
      }
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    this.timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
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
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
    }
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(getFieldCount());
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
}
