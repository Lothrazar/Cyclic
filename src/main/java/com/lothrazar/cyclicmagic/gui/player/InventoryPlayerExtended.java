package com.lothrazar.cyclicmagic.gui.player;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketSyncExtendedInventory;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class InventoryPlayerExtended implements IInventory {
  public ItemStack[] stackList;
  private Container eventHandler;
  public WeakReference<EntityPlayer> player;
  public boolean blockEvents = false;
  public static final int IROW = 4;
  public static final int ICOL = 8;
  public InventoryPlayerExtended(EntityPlayer player) {
    this.stackList = new ItemStack[IROW * ICOL + 20];
    this.player = new WeakReference<EntityPlayer>(player);
  }
  public Container getEventHandler() {
    return eventHandler;
  }
  public void setEventHandler(Container eventHandler) {
    this.eventHandler = eventHandler;
  }
  @Override
  public int getSizeInventory() {
    return this.stackList.length;
  }
  @Override
  public ItemStack getStackInSlot(int s) {
    return s >= this.getSizeInventory() ? null : this.stackList[s];
  }
  @Override
  public String getName() {
    return "";
  }
  @Override
  public boolean hasCustomName() {
    return false;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
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
  public ItemStack decrStackSize(int par1, int par2) {
    if (this.stackList[par1] != null) {
      ItemStack itemstack;
      if (this.stackList[par1].stackSize <= par2) {
        itemstack = this.stackList[par1];
        this.stackList[par1] = null;
        if (eventHandler != null)
          this.eventHandler.onCraftMatrixChanged(this);
        syncSlotToClients(par1);
        return itemstack;
      }
      else {
        itemstack = this.stackList[par1].splitStack(par2);
        if (this.stackList[par1].stackSize == 0) {
          this.stackList[par1] = null;
        }
        if (eventHandler != null)
          this.eventHandler.onCraftMatrixChanged(this);
        syncSlotToClients(par1);
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
    this.stackList[idx] = stack;
    syncSlotToClients(idx);
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void markDirty() {
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
  public void openInventory(EntityPlayer player) {
  }
  @Override
  public void closeInventory(EntityPlayer player) {
  }
  @Override
  public boolean isItemValidForSlot(int i, ItemStack stack) {
    return true;
  }
  @Override
  public int getField(int id) {
    return 0;
  }
  @Override
  public void setField(int id, int value) {
  }
  @Override
  public int getFieldCount() {
    return 0;
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
  public void dropItems(ArrayList<EntityItem> drops) {
    for (int i = 0; i < this.getSizeInventory(); ++i) {
      if (this.stackList[i] != null) {
        EntityItem ei = new EntityItem(player.get().worldObj, player.get().posX, player.get().posY + player.get().getEyeHeight(), player.get().posZ, this.stackList[i].copy());
        ei.setPickupDelay(40);
        float f1 = player.get().worldObj.rand.nextFloat() * 0.5F;
        float f2 = player.get().worldObj.rand.nextFloat() * (float) Math.PI * 2.0F;
        ei.motionX = (double) (-MathHelper.sin(f2) * f1);
        ei.motionZ = (double) (MathHelper.cos(f2) * f1);
        ei.motionY = 0.20000000298023224D;
        drops.add(ei);
        this.stackList[i] = null;
        syncSlotToClients(i);
      }
    }
  }
  public void dropItemsAt(List<EntityItem> drops, Entity e) {
    for (int i = 0; i < this.getSizeInventory(); ++i) {
      if (this.stackList[i] != null) {
        EntityItem ei = new EntityItem(e.worldObj, e.posX, e.posY + e.getEyeHeight(), e.posZ, this.stackList[i].copy());
        ei.setPickupDelay(40);
        float f1 = e.worldObj.rand.nextFloat() * 0.5F;
        float f2 = e.worldObj.rand.nextFloat() * (float) Math.PI * 2.0F;
        ei.motionX = (double) (-MathHelper.sin(f2) * f1);
        ei.motionZ = (double) (MathHelper.cos(f2) * f1);
        ei.motionY = 0.20000000298023224D;
        drops.add(ei);
        this.stackList[i] = null;
        syncSlotToClients(i);
      }
    }
  }
  public void syncSlotToClients(int slot) {
    try {
      if (ModMain.proxy.getClientWorld() == null) {
        ModMain.network.sendToAll(new PacketSyncExtendedInventory(player.get(), slot));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
