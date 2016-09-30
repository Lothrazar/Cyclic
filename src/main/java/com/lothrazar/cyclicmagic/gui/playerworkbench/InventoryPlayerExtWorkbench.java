package com.lothrazar.cyclicmagic.gui.playerworkbench;
import java.lang.ref.WeakReference;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketSyncExtendedInventory;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class InventoryPlayerExtWorkbench extends InventoryCrafting {
  public ItemStack[] stackList;
  private ContainerPlayerExtWorkbench eventHandler;
  public WeakReference<EntityPlayer> player;
  public static final int IROW = 3;
  public static final int ICOL = 3;
  public InventoryPlayerExtWorkbench(ContainerPlayerExtWorkbench containerPlayerExtWorkbench, EntityPlayer player) {
    super(containerPlayerExtWorkbench,3,3);
    this.eventHandler = containerPlayerExtWorkbench;
    this.stackList = new ItemStack[IROW * ICOL + 5];//5 armor + 3x3
    this.player = new WeakReference<EntityPlayer>(player);
  }

  @Override
  public int getSizeInventory() {
    return this.stackList.length;
  }
  @Override
  public ItemStack getStackInSlot(int s) {
    return s >= this.getSizeInventory() ? null : this.stackList[s];
  }
  /**
   * When some containers are closed they call this on each slot, then drop
   * whatever it returns as an EntityItem - like when you close a workbench GUI.
   */
  @Override
  public ItemStack removeStackFromSlot(int s) {
    if (this.stackList[s] != null) {
      ItemStack itemstack = this.stackList[s];
      this.stackList[s] = null;
      return itemstack;
    }
    else {
      return null;
    }
  }
  /**
   * Removes from an inventory slot (first arg) up to a specified number (second
   * arg) of items and returns them in a new stack.
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
//    return super.decrStackSize(index, count);
    if (this.stackList[index] != null) {
      ItemStack itemstack;
      if (this.stackList[index].stackSize <= count) {
        itemstack = this.stackList[index];
        this.stackList[index] = null;
         
          this.eventHandler.onCraftMatrixChanged(this);
       
        return itemstack;
      }
      else {
        itemstack = this.stackList[index].splitStack(count);
        if (this.stackList[index].stackSize == 0) {
          this.stackList[index] = null;
        }

          this.eventHandler.onCraftMatrixChanged(this);
      
        return itemstack;
      }
    }
    else {
      return null;
    }
  }
  /**
   * Sets the given item stack to the specified slot in the inventory (can be
   * crafting or armor sections).
   */
  @Override
  public void setInventorySlotContents(int idx, ItemStack stack) {
    if(idx>=this.stackList.length){
     //ystem.out.println("OOB CRASH"+idx);
      return;
    }
    this.stackList[idx] = stack;
    this.eventHandler.onCraftMatrixChanged(this);
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void markDirty() {
    super.markDirty();
    try {
      player.get().inventory.markDirty();
    }
    catch (Exception e) {
    }
  }
  @Override
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
    return true;
  }
  @Override
  public void clear() {
    for (int i = 0; i < stackList.length; i++) {
      stackList[i] = null;
    }
  }
  public void saveNBT(EntityPlayer player) {
    NBTTagCompound tags = player.getEntityData();
    saveNBT(tags);
  }
  public void saveNBT(NBTTagCompound tags) {
    NBTTagList tagList = new NBTTagList();
    NBTTagCompound invSlot;
    for (int i = 0; i < this.stackList.length; ++i) {
      if (this.stackList[i] != null) {
        invSlot = new NBTTagCompound();
        invSlot.setByte("Slot", (byte) i);
        this.stackList[i].writeToNBT(invSlot);
        tagList.appendTag(invSlot);
      }
    }
    tags.setTag(Const.MODID + ".Inventory", tagList);
  }
  public void readNBT(EntityPlayer player) {
    NBTTagCompound tags = player.getEntityData();
    readNBT(tags);
  }
  public void readNBT(NBTTagCompound tags) {
    NBTTagList tagList = tags.getTagList(Const.MODID + ".Inventory", 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
      int j = nbttagcompound.getByte("Slot") & 255;
      ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
      if (itemstack != null) {
        this.stackList[j] = itemstack;
      }
    }
  }
}
