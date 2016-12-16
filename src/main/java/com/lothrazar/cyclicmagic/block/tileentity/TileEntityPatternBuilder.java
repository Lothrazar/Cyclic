package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityPatternBuilder extends TileEntityBaseMachineInvo implements ITickable {
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  private int offsetX = 0;
  private int offsetY = 0;
  private int offsetZ = 1;
  private int sizeRadius = 3;
  private ItemStack[] inv;
  public static enum Fields {
    OFFX, OFFY, OFFZ, SIZER
  }
  public TileEntityPatternBuilder() {
    inv = new ItemStack[15];
  }
  @Override
  public void update() {
    BlockPos center = this.getPos().add(offsetX, offsetY, offsetZ);
    List<BlockPos> shape = UtilShape.cube(center, this.sizeRadius);
    if (this.getWorld().rand.nextDouble() < 0.1) {
      for (BlockPos p : shape) {
        UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CLOUD, p);
      }
    }
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
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
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
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP) { return new int[] { 0 }; }
    return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };//for outputting stuff
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.offsetX = tagCompound.getInteger("ox");
    this.offsetY = tagCompound.getInteger("oy");
    this.offsetZ = tagCompound.getInteger("oz");
    this.sizeRadius = tagCompound.getInteger("r");
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
    tagCompound.setInteger("ox", offsetX);
    tagCompound.setInteger("oy", offsetY);
    tagCompound.setInteger("oz", offsetZ);
    tagCompound.setInteger("r", sizeRadius);
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
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
    case OFFX:
      return this.offsetX;
    case OFFY:
      return this.offsetY;
    case OFFZ:
      return this.offsetZ;
    case SIZER:
      return this.sizeRadius;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
    case OFFX:
      this.offsetX = value;
      break;
    case OFFY:
      this.offsetY = value;
      break;
    case OFFZ:
      this.offsetZ = value;
      break;
    case SIZER:
      this.sizeRadius = value;
      break;
    default:
      break;
    }
  }
}
