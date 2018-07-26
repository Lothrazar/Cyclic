/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.playerupgrade.storage;

import java.lang.ref.WeakReference;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.InventoryBase;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import com.lothrazar.cyclicmagic.playerupgrade.PacketSyncExtendedInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class InventoryPlayerExtended extends InventoryBase implements IInventory {

  private Container eventHandler;
  public WeakReference<EntityPlayer> player;
  //public boolean blockEvents = false;
  public static final int IROW = 4;
  public static final int ICOL = 9;

  public InventoryPlayerExtended(EntityPlayer player) {
    super(IROW * ICOL + 20);//+20 somehow magically fixes bottom row
    this.player = new WeakReference<EntityPlayer>(player);
  }

  @Override
  public String getName() {
    return "Cyclic Extended Player Inventory";
  }

  public Container getEventHandler() {
    return eventHandler;
  }

  public void setEventHandler(Container eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public ITextComponent getDisplayName() {
    ITextComponent name = super.getDisplayName();
    if (name == null) {
      return new TextComponentTranslation("cyclic.inventory.extended");
    }
    return name;
  }

  /**
   * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a new stack.
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack r = super.decrStackSize(index, count);
    syncSlotToClients(index);
    return r;
  }

  /**
   * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
   */
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    super.setInventorySlotContents(index, stack);
    syncSlotToClients(index);
  }

  @Override
  public int getSizeInventory() {
    return this.inv.size();
  }

  @Override
  public ItemStack getStackInSlot(int s) {
    try {
      return s >= this.getSizeInventory() ? ItemStack.EMPTY : this.inv.get(s);
    }
    catch (Exception e) {
      ModCyclic.logger.error("[getStackInSlot] error: " + s);
      e.printStackTrace();
    }
    return ItemStack.EMPTY;
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
    catch (Exception e) {}
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
  public void setField(int id, int value) {}

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public void clear() {
    for (int i = 0; i < inv.size(); i++) {
      this.setInventorySlotContents(i, ItemStack.EMPTY);
    }
  }

  public void saveNBT(EntityPlayer player) {
    NBTTagCompound tags = player.getEntityData();
    saveNBT(tags);
  }

  public void saveNBT(NBTTagCompound tags) {
    NBTTagList tagList = new NBTTagList();
    NBTTagCompound invSlot;
    for (int i = 0; i < this.inv.size(); ++i) {
      if (!this.inv.get(i).isEmpty()) {
        invSlot = new NBTTagCompound();
        invSlot.setByte("Slot", (byte) i);
        this.inv.get(i).writeToNBT(invSlot);
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
      NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
      int j = nbttagcompound.getByte("Slot") & 255;
      ItemStack itemstack = UtilNBT.itemFromNBT(nbttagcompound);
      if (!itemstack.isEmpty()) {
        this.inv.set(j, itemstack);
      }
    }
  }

  public void dropItems(List<EntityItem> drops, BlockPos pos) {
    for (int i = 0; i < this.getSizeInventory(); ++i) {
      if (!this.inv.get(i).isEmpty()) {
        World world = player.get().getEntityWorld();
        EntityItem ei = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.inv.get(i).copy());
        ei.setPickupDelay(40);
        float f1 = world.rand.nextFloat() * 0.5F;
        float f2 = world.rand.nextFloat() * (float) Math.PI * 2.0F;
        ei.motionX = -MathHelper.sin(f2) * f1;
        ei.motionZ = MathHelper.cos(f2) * f1;
        ei.motionY = 0.20000000298023224D;
        drops.add(ei);
        this.inv.set(i, ItemStack.EMPTY);
        syncSlotToClients(i);
      }
    }
  }

  public void syncSlotToClients(int slot) {
    try {
      if (ModCyclic.proxy.getClientWorld() == null) {
        ModCyclic.network.sendToAll(new PacketSyncExtendedInventory(player.get(), slot));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
