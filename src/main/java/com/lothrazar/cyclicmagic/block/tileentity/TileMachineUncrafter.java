package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class TileMachineUncrafter extends TileEntityBaseMachineInvo implements  ITileRedstoneToggle {
  // http://www.minecraftforge.net/wiki/Containers_and_GUIs
  // http://greyminecraftcoder.blogspot.com.au/2015/01/tileentity.html
  // http://www.minecraftforge.net/forum/index.php?topic=28539.0
  // http://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/tile-entities/
  // http://www.minecraftforge.net/wiki/Tile_Entity_Synchronization
  // http://www.minecraftforge.net/forum/index.php?topic=18871.0
  public static int TIMER_FULL;
  private ItemStack[] inv;
  private int timer;
  private int needsRedstone = 1;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  public static enum Fields {
    TIMER, REDSTONE
  }
  public TileMachineUncrafter() {
    inv = new ItemStack[9];
    timer = TIMER_FULL;
  }
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
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
    // this is from interface IUpdatePlayerListBox
    // change up the timer on both client and server (it gets synced
    // eventually but not constantly)
    this.shiftAllUp();
    boolean triggerUncraft = false;
    if (this.onlyRunIfPowered() && this.isPowered() == false) {
      //it works ONLY if its powered
      return;
    }
    //else: its powered, OR it doesnt need power so its ok
    ItemStack stack = getStackInSlot(0);
    if (stack == null) {
      return;
    }
    timer--;
    if (timer <= 0) {
      timer = TIMER_FULL;
      triggerUncraft = true;
    }
    if (triggerUncraft) {
      BlockPos posOffsetFacing = this.getCurrentFacingPos();//new BlockPos(x, y, z);
      TileEntity attached = this.worldObj.getTileEntity(posOffsetFacing);
      IInventory attachedInv = null;
      if (attached != null && attached instanceof IInventory) {
        attachedInv = (IInventory) attached;
      }
      UtilUncraft uncrafter = new UtilUncraft(stack);
      boolean success = false;
      try {
        success = uncrafter.doUncraft();
      }
      catch (Exception e) {
        ModMain.logger.error("Unhandled exception in uncrafting " + e.getStackTrace().toString());
      }
      if (success) {
        if (this.worldObj.isRemote == false) { // drop the items
          ArrayList<ItemStack> uncrafterOutput = uncrafter.getDrops();
          ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
          if (attached != null) {
            toDrop = dumpToIInventory(uncrafterOutput, attachedInv);
          }
          else {
            toDrop = uncrafterOutput;
          }
          for (ItemStack s : toDrop) {
            UtilEntity.dropItemStackInWorld(worldObj, posOffsetFacing, s);
          }
          this.decrStackSize(0, uncrafter.getOutsize());
        }
        UtilSound.playSound(worldObj, this.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS);
      }
      else {//try to dump to inventory first
        if (attached != null) {
          ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
          toDrop.add(stack);
          toDrop = dumpToIInventory(toDrop, attachedInv);
          if (toDrop.size() == 1) {//it only had one in it. so if theres one left, it didnt work
            UtilEntity.dropItemStackInWorld(worldObj, posOffsetFacing, toDrop.get(0));
          }
        }
        else {
          UtilEntity.dropItemStackInWorld(worldObj, posOffsetFacing, stack);
        }
        if (this.worldObj.isRemote == false) {
          this.decrStackSize(0, stack.stackSize);
        }
        UtilSound.playSound(worldObj, this.getPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS);
      }
      this.worldObj.markBlockRangeForRenderUpdate(this.getPos(), this.getPos().up());
      this.markDirty();
    }
    else {
      this.spawnParticlesAbove();// its still processing
    }
  }
  public static ArrayList<ItemStack> dumpToIInventory(ArrayList<ItemStack> stacks, IInventory inventory) {
    //and return the remainder after dumping
    ArrayList<ItemStack> remaining = new ArrayList<ItemStack>();
    ItemStack chestStack;
    for (ItemStack current : stacks) {
      if (current == null) {
        continue;
      }
      for (int i = 0; i < inventory.getSizeInventory(); i++) {
        if (current == null) {
          continue;
        }
        chestStack = inventory.getStackInSlot(i);
        if (chestStack == null) {
          inventory.setInventorySlotContents(i, current);
          // and dont add current ot remainder at all ! sweet!
          current = null;
        }
        else if (chestStack.isItemEqual(current)) {
          int space = chestStack.getMaxStackSize() - chestStack.stackSize;
          int toDeposit = Math.min(space, current.stackSize);
          if (toDeposit > 0) {
            current.stackSize -= toDeposit;
            chestStack.stackSize += toDeposit;
            if (current.stackSize == 0) {
              current = null;
            }
          }
        }
      } // finished current pass over inventory
      if (current != null) {
        remaining.add(current);
      }
    }
    return remaining;
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }
  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn);
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        if (needsRedstone != 1 && needsRedstone != 0) {
          needsRedstone = 0;
        }
        return this.needsRedstone;
      }
    return -7;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
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
  private boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
