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

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.state.IBlockState;
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
    //    tileEntity.addInvo(this);
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
    ItemStack stackAfter = tileEntity.decrStackSize(index, count);
    if (stackAfter != ItemStack.EMPTY) {
      tileEntity.syncAllCraftSlots();
    }
    return stackAfter;
  }

  @Override
  public void markDirty() {
    tileEntity.markDirty();
    IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
    tileEntity.getWorld().notifyBlockUpdate(tileEntity.getPos(), state, state, 3);
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.tileEntity.setInventorySlotContents(index, stack);
    tileEntity.syncAllCraftSlots();
  }

  @Override
  public void openInventory(EntityPlayer player) {
    tileEntity.addInvo(this);
    ModCyclic.logger.info("OOOPPPPEEEENNNNNN  " + tileEntity);
    super.openInventory(player);
  }

  @Override
  public void closeInventory(EntityPlayer player) {
    super.closeInventory(player);
    tileEntity.removeInvo(this);
  }
}
