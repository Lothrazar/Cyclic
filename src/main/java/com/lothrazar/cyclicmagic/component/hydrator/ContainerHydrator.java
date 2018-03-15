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
package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHydrator extends ContainerBaseMachine {
  static final int MID_SPACING = 133;
  static final int SLOTX_FLUID = 70;
  static final int SLOTY_FLUID = 39;
  static final int SLOTX_START = 8;
  public static final int SLOTY = 30;
  static final int SQ = 18;
  public ContainerHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator te) {
    super(te);
    int slotNum = 0;
    for (int i = 0; i < TileEntityHydrator.RECIPE_SIZE; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          SLOTX_START + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    for (int i = 0; i < TileEntityHydrator.RECIPE_SIZE; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          MID_SPACING + 1 + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    addSlotToContainer(new Slot(tile, slotNum,
        SLOTX_FLUID,
        SLOTY_FLUID));
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity 
      if (slot < tile.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tile.getSizeInventory(), 36 + tile.getSizeInventory(), true)) {
          return ItemStack.EMPTY;
        }
      }
      else if (UtilFluid.getFluidType(stackInSlot) == FluidRegistry.WATER) {// shortcut to middle stack if water
        if (!this.mergeItemStack(stackInSlot, 8, 9, true)) {
          return ItemStack.EMPTY;
        }
      }
      else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory(), false)) {
        return ItemStack.EMPTY;
      }
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(ItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) {
        return ItemStack.EMPTY;
      }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tile.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tile);
  }
}
