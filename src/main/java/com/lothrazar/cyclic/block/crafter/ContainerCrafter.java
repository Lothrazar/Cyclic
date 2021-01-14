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

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
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

  public ContainerCrafter(int windowId, World clientWorld, BlockPos pos, PlayerInventory inv, PlayerEntity clientPlayer) {
    super(ContainerScreenRegistry.crafter, windowId);
    tile = (TileCrafter) clientWorld.getTileEntity(pos);
    trackEnergy(tile);
    this.playerEntity = clientPlayer;
    this.playerInventory = inv;
    //    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.INPUT).ifPresent(h -> {
    //    ItemStackHandler in = tile.inputHandler;  
    int indexx = 0;
    //add input
    for (int rowPos = 0; rowPos < TileCrafter.IO_NUM_ROWS; rowPos++) {
      for (int colPos = 0; colPos < TileCrafter.IO_NUM_COLS; colPos++) {
        this.addSlot(new SlotItemHandler(tile.inputHandler, indexx,
            INPUT_START_X + colPos * Const.SQ,
            INPUT_START_Y + rowPos * Const.SQ));
        indexx++;
      }
    }
    //add grid
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.GRID).ifPresent(h -> {
      int index = 0;
      for (int rowPos = 0; rowPos < TileCrafter.GRID_NUM_ROWS; rowPos++) {
        for (int colPos = 0; colPos < TileCrafter.GRID_NUM_ROWS; colPos++) {
          this.addSlot(new CrafterGridSlot(h, index,
              GRID_START_X + colPos * Const.SQ,
              GRID_START_Y + rowPos * Const.SQ));
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
            OUTPUT_START_Y + rowPos * Const.SQ));
        indexx++;
      }
    }
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, TileCrafter.ItemHandlers.PREVIEW).ifPresent(h -> {
      addSlot(new CrafterGridSlot(h, 0,
          PREVIEW_START_X,
          PREVIEW_START_Y));
    });
    this.endInv = inventorySlots.size();
    layoutPlayerInventorySlots(8, 153);
    this.trackAllIntFields(tile, TileCrafter.Fields.values().length);
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (slotId == TileCrafter.PREVIEW_SLOT) {
      return ItemStack.EMPTY;
    }
    if (slotId >= TileCrafter.GRID_SLOT_START && slotId <= TileCrafter.GRID_SLOT_STOP) {
      ItemStack ghostStack = player.inventory.getItemStack().copy();
      ghostStack.setCount(1);
      inventorySlots.get(slotId).putStack(ghostStack);
      tile.shouldSearch = true;
      return ItemStack.EMPTY;
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
    return slotIn.getSlotIndex() != TileCrafter.PREVIEW_SLOT && super.canMergeSlot(stack, slotIn);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.crafter);
  }
}
