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
package com.lothrazar.cyclicmagic.block.fishing;

import com.lothrazar.cyclicmagic.gui.core.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotCheckTileValid;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFisher extends ContainerBaseMachine {

  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 44;
  public static final int SLOTY = 20;
  public static final int SLOTX_FISH = 62;
  public static final int SLOTY_FISH = 20;

  public ContainerFisher(InventoryPlayer inventoryPlayer, TileEntityFishing te) {
    super(te);
    //the first slot is the rod
    addSlotToContainer(new SlotCheckTileValid(tile, TileEntityFishing.SLOT_TOOL, SLOTX_START, SLOTY));
    int s = 1;
    int row = 0, col = 0;
    for (int i = 0; i < TileEntityFishing.FISHSLOTS; i++) { //so going from 0-9
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      addSlotToContainer(new SlotOutputOnly(tile, s, SLOTX_FISH + row * Const.SQ, SLOTY_FISH + col * Const.SQ));
      s++;
    }
    // commonly used vanilla code that adds the player's inventory
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
      // places it into the tileEntity is possible since its in the player
      // inventory
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
}
