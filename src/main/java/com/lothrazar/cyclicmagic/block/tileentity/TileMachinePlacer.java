package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileMachinePlacer extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  private static final int buildSpeed = 1;
  public static final int TIMER_FULL = 75;//one day i will add fuel AND/OR speed upgrades. till then make very slow
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  public static enum Fields {
    TIMER, REDSTONE
  }
  private int timer;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots
  private int needsRedstone = 1;
  public TileMachinePlacer() {
    super(9);
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) != null;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
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
    if (id >= 0 && id < this.getFieldCount())
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
  public int getFieldCount() {
    return Fields.values().length;
  }
  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    shiftAllUp();
    boolean trigger = false;
    if (!isRunning()) {
      // it works ONLY if its powered
      markDirty();
      return;
    }
    this.spawnParticlesAbove();// its still processing
    ItemStack stack = getStackInSlot(0);
    if (stack == null) {
      timer = TIMER_FULL;// reset just like you would in a
      // furnace
    }
    else {
      timer -= buildSpeed;
      if (timer <= 0) {
        timer = TIMER_FULL;
        trigger = true;
      }
    }
    if (trigger) {
      Block stuff = Block.getBlockFromItem(stack.getItem());
      if (stuff != null) {//&&getWorld().isRemote == false
        if (UtilPlaceBlocks.placeStateSafe(getWorld(), null, pos.offset(this.getCurrentFacing()),
            UtilItemStack.getStateFromMeta(stuff, stack.getMetadata()))) {// stuff.getStateFromMeta(stack.getMetadata()))) {
          if (getWorld().isRemote == false) {
            this.decrStackSize(0, 1);
          }
        }
      }
    }
    this.markDirty();
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }
  @Override
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else {
      return super.receiveClientEvent(id, value);
    }
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
