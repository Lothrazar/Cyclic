package com.lothrazar.cyclicmagic.component.uncrafter;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import com.lothrazar.cyclicmagic.util.UtilUncraft.UncraftResultType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityUncrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  // http://www.minecraftforge.net/wiki/Containers_and_GUIs
  // http://greyminecraftcoder.blogspot.com.au/2015/01/tileentity.html
  // http://www.minecraftforge.net/forum/index.php?topic=28539.0
  // http://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/tile-entities/
  // http://www.minecraftforge.net/wiki/Tile_Entity_Synchronization
  // http://www.minecraftforge.net/forum/index.php?topic=18871.0
  public static final int SLOT_UNCRAFTME = 0;
  public static final int SLOT_ROWS = 3;
  public static final int SLOT_COLS = 7;
  public static final int TIMER_FULL = 200;
  private int needsRedstone = 1;
  private int[] hopperInput = { 0 };
  private int[] hopperOutput;
  public static enum Fields {
    TIMER, REDSTONE, FUEL, FUELMAX;
  }
  public TileEntityUncrafter() {
    super(SLOT_ROWS * SLOT_COLS + 2);
    timer = TIMER_FULL;
    hopperOutput = new int[SLOT_ROWS * SLOT_COLS];
    for (int i = 1; i <= SLOT_ROWS * SLOT_COLS; i++) {
      hopperOutput[i - 1] = i;
    }
    this.setFuelSlot(SLOT_ROWS * SLOT_COLS + 1, BlockUncrafting.FUEL_COST);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
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
  @Override
  public void update() {
    if (!this.isRunning()) {
      return;
    }
    //else: its powered, OR it doesnt need power so its ok
    ItemStack stack = getStackInSlot(SLOT_UNCRAFTME);
    if (stack.isEmpty()) {
      return;
    }
    this.spawnParticlesAbove();// its processing
    this.updateFuelIsBurning();
    if (this.updateTimerIsZero()) {
      timer = TIMER_FULL; //reset the timer and do the thing
      UtilUncraft.Uncrafter uncrafter = new UtilUncraft.Uncrafter();
      try {
        if (uncrafter.process(stack) == UncraftResultType.SUCCESS) {
          if (this.getWorld().isRemote == false) { // drop the items
            ArrayList<ItemStack> uncrafterOutput = uncrafter.getDrops();
            setOutputItems(uncrafterOutput);
            this.decrStackSize(0, uncrafter.getOutsize());
            UtilSound.playSoundFromServer(SoundRegistry.crack, SoundCategory.BLOCKS, this.getPos(), this.getDimension(), 16);
          }
          //          UtilSound.playSound(getWorld(), this.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS);
        }
        else {//success = false, so try to dump to inventory first
          ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
          toDrop.add(stack.copy());
          setOutputItems(toDrop);
          if (this.getWorld().isRemote == false) {
            this.decrStackSize(0, stack.getCount());
          }
        }
        this.getWorld().markBlockRangeForRenderUpdate(this.getPos(), this.getPos().up());
        this.markDirty();
      }
      catch (Exception e) {
        ModCyclic.logger.error("Unhandled exception in uncrafting ");
        ModCyclic.logger.error(e.getMessage());
        e.printStackTrace();
      }
    } //end of timer go
  }
  private void setOutputItems(List<ItemStack> output) {
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(output, this, SLOT_UNCRAFTME + 1);
    if (!toDrop.isEmpty()) {
      for (ItemStack s : toDrop) {
        UtilItemStack.dropItemStackInWorld(this.getWorld(), this.getPos().up(), s);
      }
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP)
      return hopperInput;//input through top side
    return hopperOutput;
  }
  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        if (needsRedstone != 1 && needsRedstone != 0) {
          needsRedstone = 0;
        }
        return this.needsRedstone;
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
    }
    return -7;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        if (value != 1 && value != 0) {
          value = 0;
        }
        this.needsRedstone = value;
      break;
      case FUEL:
        this.setFuelCurrent(value);
      break;
      case FUELMAX:
      break;
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
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
}
