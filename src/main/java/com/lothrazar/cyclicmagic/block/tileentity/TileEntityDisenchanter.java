package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
    if (!isInputValid()) { return; }
    this.spawnParticlesAbove();
    //odo; stop here depending on item state?
    timer -= 1;
    if (timer > 0) { return; }
    timer = TIMER_FULL;
    World world = this.getWorld();
    //now go my pretty!
    
    this.decrStackSize(SLOT_GLOWSTONE);
    this.decrStackSize(SLOT_REDSTONE);
    this.decrStackSize(SLOT_GPOWDER);
    this.decrStackSize(SLOT_BOOK);
    //the good stuff goes here  
    //TODO  ench movving
    
    
    
    // only drop input IF it has zero chants left eh
    UtilItemStack.dropItemStackInWorld(world, this.pos, this.getStackInSlot(SLOT_INPUT));
    this.setInventorySlotContents(SLOT_INPUT, ItemStack.EMPTY);
    
    //TODO: drop the new enchanted book
    
    //always drop book, one single enchant per book
//    UtilItemStack.dropItemStackInWorld(world, this.pos, .....);
    
  }
  private boolean isInputValid() {
    return this.getStackInSlot(SLOT_BOOK).getItem() == Items.BOOK
        && this.getStackInSlot(SLOT_REDSTONE).getItem() == Items.REDSTONE
        && this.getStackInSlot(SLOT_GLOWSTONE).getItem() == Items.GLOWSTONE_DUST
        && this.getStackInSlot(SLOT_GPOWDER).getItem() == Items.GUNPOWDER
        && this.getStackInSlot(SLOT_INPUT).isEmpty() == false;
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
