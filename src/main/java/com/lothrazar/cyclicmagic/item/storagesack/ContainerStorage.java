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
package com.lothrazar.cyclicmagic.item.storagesack;

import com.lothrazar.cyclicmagic.core.gui.ContainerBase;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilPlayer;
import com.lothrazar.cyclicmagic.gui.slot.SlotItemRestrictedInverse;
import com.lothrazar.cyclicmagic.module.ItemModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerStorage extends ContainerBase {

  final InventoryStorage inventory;
  final static int INV_START = InventoryStorage.INV_SIZE, INV_END = INV_START + 26,
      HOTBAR_START = INV_END + 1,
      HOTBAR_END = HOTBAR_START + 8;
  final static int pad = 8;
  final static int hotbar = 9;
  final static int rows = 6;
  final static int cols = 11;

  public ContainerStorage(EntityPlayer par1Player, InventoryPlayer playerInventory, InventoryStorage invoWand) {
    this.inventory = invoWand;
    int x, y = pad, k, l, slot;
    // start the main container area
    for (l = 0; l < rows; ++l) {
      for (k = 0; k < cols; ++k) {
        x = pad + k * Const.SQ;
        y = pad + l * Const.SQ;
        slot = k + l * cols;
        this.addSlotToContainer(new SlotItemRestrictedInverse(invoWand, slot, x, y, ItemModule.storage_bag));
      }
    }
    int yBase = pad + rows * Const.SQ + 14;
    // start the players inventory
    for (l = 0; l < 3; ++l) {
      for (k = 0; k < hotbar; ++k) {
        x = pad + (k + 1) * Const.SQ;
        y = l * Const.SQ + yBase;
        slot = k + l * hotbar + hotbar;
        this.addSlotToContainer(new Slot(playerInventory, slot, x, y));
      }
    }
    // players hotbar
    int yhotbar = yBase + 3 * Const.SQ + pad / 2;
    for (k = 0; k < hotbar; ++k) {
      slot = k;
      x = pad + (k + 1) * Const.SQ;
      this.addSlotToContainer(new Slot(playerInventory, slot, x, yhotbar));
    }
  }

  @Override
  public ItemStack slotClick(int slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    ItemStack wand = UtilPlayer.getPlayerItemIfHeld(player);
    // this will prevent the player from interacting with the item that
    // opened the inventory:
    if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == wand) {
      return ItemStack.EMPTY;
    }
    return super.slotClick(slot, dragType, clickTypeIn, player);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;//inventory.isUseableByPlayer(playerIn);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      // If item is in our custom Inventory or armor slot
      if (index < INV_START) {
        // try to place in player inventory / action bar
        if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) {
          inventory.markDirty();
          return ItemStack.EMPTY;
        }
        slot.onSlotChange(itemstack1, itemstack);
      }
      // Item is in inventory / hotbar, try to place in custom inventory
      // or armor slots
      else {
        if (index >= INV_START) {
          // place in custom inventory
          if (!this.mergeItemStack(itemstack1, 0, INV_START, false)) {
            inventory.markDirty();
            return ItemStack.EMPTY;
          }
        }
        if (index >= INV_START && index < HOTBAR_START) {
          // place in action bar
          if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END + 1, false)) {
            inventory.markDirty();
            return ItemStack.EMPTY;
          }
        }
        // item in action bar - place in player inventory
        else if (index >= HOTBAR_START && index < HOTBAR_END + 1) {
          if (!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)) {
            inventory.markDirty();
            return ItemStack.EMPTY;
          }
        }
      }
      if (itemstack1.getCount() == 0) {
        slot.putStack(ItemStack.EMPTY);
      }
      else {
        slot.onSlotChanged();
      }
      if (itemstack1.getCount() == itemstack.getCount()) {
        inventory.markDirty();
        return ItemStack.EMPTY;
      }
      slot.onTake(par1EntityPlayer, itemstack1);
    }
    inventory.markDirty();
    return itemstack;
  }
}
