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
package com.lothrazar.cyclicmagic.block.workbench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

//to save code just extend vanilla workbench instead of remake
public class InventoryWorkbench extends InventoryCrafting {

  private TileEntityWorkbench tileEntity;
  private Container container;

  public InventoryWorkbench(Container eventHandlerIn, TileEntityWorkbench tileEntity) {
    super(eventHandlerIn, 3, 3);
    this.tileEntity = tileEntity;
    container = eventHandlerIn;
  }

  @Override
  public int getSizeInventory() {
    return 9;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return this.tileEntity.getStackInSlot(index);
  }

  /**
   * Sync crafting table changes between multiple players in use
   */
  public void onCraftMatrixChanged() {
    container.onCraftMatrixChanged(this);
  }

  /**
   * just like vanilla
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
    if (this.getStackInSlot(index).isEmpty()) {
      return ItemStack.EMPTY;
    }
    ItemStack stack;
    if (this.getStackInSlot(index).getCount() <= count) {
      stack = this.getStackInSlot(index);
      this.setInventorySlotContents(index, ItemStack.EMPTY);
      this.onCraftMatrixChanged();
      return stack;
    }
    else {
      stack = this.getStackInSlot(index).splitStack(count);
      if (this.getStackInSlot(index).getCount() == 0) {
        this.setInventorySlotContents(index, ItemStack.EMPTY);
      }
      this.onCraftMatrixChanged();
      return stack;
    }
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.tileEntity.setInventorySlotContents(index, stack);
    this.onCraftMatrixChanged();
  }

  @Override
  public void openInventory(EntityPlayer player) {
    super.openInventory(player);
    tileEntity.addInvo(this);
  }

  @Override
  public void closeInventory(EntityPlayer player) {
    super.closeInventory(player);
    tileEntity.removeInvo(this);
  }
}
