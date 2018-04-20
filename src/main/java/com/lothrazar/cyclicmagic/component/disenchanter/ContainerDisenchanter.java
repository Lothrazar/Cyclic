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
package com.lothrazar.cyclicmagic.component.disenchanter;

import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotItemRestricted;
import com.lothrazar.cyclicmagic.gui.slot.SlotOnlyEnchanted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDisenchanter extends ContainerBaseMachine {

  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_INPUT = 0;
  public static final int SLOTX_BOTTLE = 1;
  public static final int SLOTX_REDSTONE = 2;
  public static final int SLOTX_GLOWSTONE = 3;
  public static final int SLOTX_BOOK = 4;
  public static final int SLOTY_INPUT = 0;
  public static final int SLOTY_BOTTLE = 1;
  public static final int SLOTY_REDSTONE = 2;
  public static final int SLOTY_GLOWSTONE = 3;
  public static final int SLOTY_BOOK = 4;

  public ContainerDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter te) {
    super(te);
    this.setScreenSize(ScreenSize.LARGE);
    Item itemFiltered = null;
    int x = 0, y = 0, ystart = 20, spacing = 26;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      switch (i) {
        case TileEntityDisenchanter.SLOT_BOOK://center center
          itemFiltered = Items.BOOK;
          x = getScreenSize().width() / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          itemFiltered = Items.GLOWSTONE_DUST;
          x = getScreenSize().width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          itemFiltered = Items.EXPERIENCE_BOTTLE;
          x = getScreenSize().width() / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          itemFiltered = Items.REDSTONE;
          x = getScreenSize().width() - getScreenSize().width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          itemFiltered = Items.ENCHANTED_BOOK;//not reallyjust the book
          x = getScreenSize().width() / 2;
          y = ystart;
        break;
        default:
          itemFiltered = null;
          x = Const.PAD + (i - 5) * Const.SQ;
          y = ystart + 3 * spacing - 1;
        break;
      }
      if (itemFiltered == null) {
        addSlotToContainer(new Slot(te, i, x, y));
      }
      else if (itemFiltered == Items.ENCHANTED_BOOK) {
        addSlotToContainer(new SlotOnlyEnchanted(tile, i, x, y));
      }
      else {
        addSlotToContainer(new SlotItemRestricted(tile, i, x, y, itemFiltered));
      }
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
