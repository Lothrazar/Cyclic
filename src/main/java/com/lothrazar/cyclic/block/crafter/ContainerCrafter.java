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
package com.lothrazar.cyclic.block.crafter;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrafter extends ContainerBase {

  TileCrafter tile;
  public static final int INPUT_START_X = 8;
  public static final int INPUT_START_Y = 35;
  public static final int GRID_START_X = 52;
  public static final int GRID_START_Y = 71;
  public static final int OUTPUT_START_X = 114;
  public static final int OUTPUT_START_Y = 35;
  public static final int PREVIEW_START_X = 70;
  public static final int PREVIEW_START_Y = 35;

  public ContainerCrafter(int windowId, Level clientWorld, BlockPos pos, Inventory inv, Player clientPlayer) {
    super(MenuTypeRegistry.CRAFTER.get(), windowId);
    tile = (TileCrafter) clientWorld.getBlockEntity(pos);
    trackEnergy(tile);
    this.endInv = TileCrafter.IO_NUM_COLS * TileCrafter.IO_NUM_ROWS;
    this.playerEntity = clientPlayer;
    this.playerInventory = inv;
    int indexx = 0;
    //add input
    for (int rowPos = 0; rowPos < TileCrafter.IO_NUM_ROWS; rowPos++) {
      for (int colPos = 0; colPos < TileCrafter.IO_NUM_COLS; colPos++) {
        this.addSlot(new SlotItemHandler(tile.inputHandler, indexx,
            INPUT_START_X + colPos * Const.SQ,
            INPUT_START_Y + rowPos * Const.SQ) {

          @Override
          public void setChanged() {
            tile.setChanged();
          }
        });
        indexx++;
      }
    }
    //add grid
    tile.getCapability(ForgeCapabilities.ITEM_HANDLER, TileCrafter.ItemHandlers.GRID).ifPresent(h -> {
      int index = 0;
      for (int rowPos = 0; rowPos < TileCrafter.GRID_NUM_ROWS; rowPos++) {
        for (int colPos = 0; colPos < TileCrafter.GRID_NUM_ROWS; colPos++) {
          this.addSlot(new CrafterGridSlot(h, index,
              GRID_START_X + colPos * Const.SQ,
              GRID_START_Y + rowPos * Const.SQ) {

            @Override
            public void setChanged() {
              tile.setChanged();
            }
          });
          index++;
        }
      }
    });
    //add output 
    indexx = 0;
    for (int rowPos = 0; rowPos < TileCrafter.IO_NUM_ROWS; rowPos++) {
      for (int colPos = 0; colPos < TileCrafter.IO_NUM_COLS; colPos++) {
        this.addSlot(new CrafterOutputSlot(tile.outHandler, indexx,
            OUTPUT_START_X + colPos * Const.SQ,
            OUTPUT_START_Y + rowPos * Const.SQ) {

          @Override
          public void setChanged() {
            tile.setChanged();
          }
        });
        indexx++;
      }
    }
    tile.getCapability(ForgeCapabilities.ITEM_HANDLER, TileCrafter.ItemHandlers.PREVIEW).ifPresent(h -> {
      addSlot(new CrafterGridSlot(h, 0,
          PREVIEW_START_X,
          PREVIEW_START_Y) {

        @Override
        public void setChanged() {
          tile.setChanged();
        }
      });
    });
    this.endInv = slots.size();
    layoutPlayerInventorySlots(8, 153);
    this.trackAllIntFields(tile, TileCrafter.Fields.values().length);
  }

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    //30-65 is players
    //29 is the preview slot
    //19-28 is the right output
    //10-18 is crafting grid
    //0-9 is input leftc
    int playerStart = endInv;
    int playerEnd = endInv + PLAYERSIZE;
    //standard logic based on start/end
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot != null && slot.hasItem()) {
      ItemStack stack = slot.getItem();
      itemstack = stack.copy();
      //from output to player
      if (index >= TileCrafter.OUTPUT_SLOT_START && index <= TileCrafter.OUTPUT_SLOT_STOP) {
        if (!this.moveItemStackTo(stack, playerStart, playerEnd, false)) {
          return ItemStack.EMPTY;
        }
      }
      //from input to player
      if (index < TileCrafter.IO_SIZE) {
        if (!this.moveItemStackTo(stack, playerStart, playerEnd, false)) {
          return ItemStack.EMPTY;
        }
      }
      else if (index <= playerEnd && !this.moveItemStackTo(stack, 0, 9, false)) {
        return ItemStack.EMPTY;
      }
      if (stack.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      }
      else {
        slot.setChanged();
      }
      if (stack.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }
      slot.onTake(playerIn, stack);
    }
    return itemstack;
  }

  @Override
  public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
    if (slotId == TileCrafter.PREVIEW_SLOT) {
      return; //ItemStack.EMPTY;
    }
    // [ 10 - 18 ]
    if (slotId >= TileCrafter.GRID_SLOT_START && slotId <= TileCrafter.GRID_SLOT_STOP) {
      ItemStack ghostStack = player.containerMenu.getCarried().copy();
      ghostStack.setCount(1);
      slots.get(slotId).set(ghostStack);
      //      tile.shouldSearch = true;
      return; //ItemStack.EMPTY;
    }
    super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
    // dont merge to preview
    // dont merge to grid
    return slotIn.getSlotIndex() != TileCrafter.PREVIEW_SLOT &&
        !(slotIn.getSlotIndex() >= TileCrafter.GRID_SLOT_START && slotIn.getSlotIndex() <= TileCrafter.GRID_SLOT_STOP) &&
        super.canTakeItemForPickAll(stack, slotIn);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.CRAFTER.get());
  }
}
