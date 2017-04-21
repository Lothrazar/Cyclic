package com.lothrazar.cyclicmagic.block.tileentity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntityDisenchanter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public static enum Fields {
    REDSTONE, TIMER
  }
  public static final int TIMER_FULL = 100;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_GPOWDER = 1;
  public static final int SLOT_REDSTONE = 2;
  public static final int SLOT_GLOWSTONE = 3;
  public static final int SLOT_BOOK = 4;
  private int needsRedstone = 1;
  private int timer;
  public TileEntityDisenchanter() {
    super(5);
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    this.spawnParticlesAbove();
    //odo; stop here depending on item state?
    timer -= 1;
    if (timer > 0) { return; }
    timer = TIMER_FULL;
    World world = this.getWorld();
    //now go my pretty!
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP) {
      return new int[] { 0 };
    }
    else if (side == EnumFacing.DOWN) { return new int[] { 2 }; }
    return new int[] { 1 };//for outputting stuff
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
      default:
      break;
    }
  }
}
