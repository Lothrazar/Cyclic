package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import com.lothrazar.cyclicmagic.util.UtilUncraft.UncraftResultType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileMachineUncrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  // http://www.minecraftforge.net/wiki/Containers_and_GUIs
  // http://greyminecraftcoder.blogspot.com.au/2015/01/tileentity.html
  // http://www.minecraftforge.net/forum/index.php?topic=28539.0
  // http://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/tile-entities/
  // http://www.minecraftforge.net/wiki/Tile_Entity_Synchronization
  // http://www.minecraftforge.net/forum/index.php?topic=18871.0
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  public static final int SLOT_UNCRAFTME = 0;
  public static final int SLOT_ROWS = 3;
  public static final int SLOT_COLS = 7;
  public static int TIMER_FULL;
  private int timer;
  private int needsRedstone = 1;
  private int[] hopperInput = { 0 };
  private int[] hopperOutput;
  public static enum Fields {
    TIMER, REDSTONE;//, UNCRAFTRESULT;
  }
  public TileMachineUncrafter() {
    super(SLOT_ROWS * SLOT_COLS + 1);
    timer = TIMER_FULL;
    hopperOutput = new int[SLOT_ROWS * SLOT_COLS];
    for (int i = 1; i <= SLOT_ROWS * SLOT_COLS; i++) {
      hopperOutput[i - 1] = i;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
  }
  public int getTimer() {
    return timer;
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    if (!this.isRunning()) {
      //it works ONLY if its powered
      return;
    }
    //else: its powered, OR it doesnt need power so its ok
    ItemStack stack = getStackInSlot(SLOT_UNCRAFTME);
    if (stack.isEmpty()) { return; }
    this.spawnParticlesAbove();// its processing
    this.decrTimer();
    if (timer <= 0) {
      timer = TIMER_FULL; //reset the timer and do the thing
      UtilUncraft.Uncrafter uncrafter = new UtilUncraft.Uncrafter();
      boolean success = false;
      try {
        success = uncrafter.process(stack) == UncraftResultType.SUCCESS;
        if (success) {
          if (this.getWorld().isRemote == false) { // drop the items
            ArrayList<ItemStack> uncrafterOutput = uncrafter.getDrops();
            setOutputItems(uncrafterOutput);
            this.decrStackSize(0, uncrafter.getOutsize());
          }
          UtilSound.playSound(getWorld(), this.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS);
        }
        else {//success = false, so try to dump to inventory first
          ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
          toDrop.add(stack);
          setOutputItems(toDrop);
          if (this.getWorld().isRemote == false) {
            this.decrStackSize(0, stack.getCount());
          }
          UtilSound.playSound(this.getWorld(), this.getPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS);
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
  private void decrTimer() {
    if (this.getWorld().isRemote == false) {
      timer--;
    }
  }
  private void setOutputItems(ArrayList<ItemStack> output) {
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(output, this, SLOT_UNCRAFTME + 1);
    if (toDrop != null)
      for (ItemStack s : toDrop) {
      UtilItemStack.dropItemStackInWorld(this.getWorld(), this.getPos().up(), s);
      }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP)
      return hopperInput;//input through top side
    else
      return hopperOutput;
  }
  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn);
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          return timer;
        case REDSTONE:
          if (needsRedstone != 1 && needsRedstone != 0) {
            needsRedstone = 0;
          }
          return this.needsRedstone;
        //      case UNCRAFTRESULT:
        //        return this.uncraftResult ;
        default:
        break;
      }
    }
    return -7;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
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
        //      case UNCRAFTRESULT:
        //        this.uncraftResult = value;
        //        break;
        default:
        break;
      }
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
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
