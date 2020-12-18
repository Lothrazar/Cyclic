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
package com.lothrazar.cyclic.net;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCraftAction extends PacketBase {

  //  private BlockPos pos = BlockPos.ZERO;
  private CraftingActionEnum action;

  public PacketCraftAction(CraftingActionEnum s) {
    action = s;
  }

  public static PacketCraftAction decode(PacketBuffer buf) {
    return new PacketCraftAction(CraftingActionEnum.values()[buf.readInt()]);
  }

  public static void encode(PacketCraftAction msg, PacketBuffer buf) {
    buf.writeInt(msg.action.ordinal());
  }

  public static void handle(PacketCraftAction message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      ServerPlayerEntity sender = ctx.get().getSender();
      if (sender.openContainer instanceof IContainerCraftingAction) {
        //do the thing
        IContainerCraftingAction c = (IContainerCraftingAction) sender.openContainer;
        performAction(c, sender, message.action);
      }
    });
    message.done(ctx);
  }

  private static void performAction(IContainerCraftingAction c, PlayerEntity player, CraftingActionEnum action) {
    //call this serverside from a packet
    switch (action) {
      case EMPTY:
        // move all slots down out of grid into inventory
        for (int i = 1; i <= 9; i++) {
          c.transferStackInSlot(player, i);
        }
        c.getCraftResult().clear();
      break;
      case SPREAD:
        balanceLargestSlot(c);
      break;
    }
  }

  private static void balanceLargestSlot(IContainerCraftingAction c) {
    //step 1: find the stack with the largest size.
    ItemStack biggest = ItemStack.EMPTY;
    int foundSlot = -1;
    for (int i = 0; i <= 8; i++) {
      ItemStack tmp = c.getCraftMatrix().getStackInSlot(i);
      if (!tmp.isEmpty() && tmp.getCount() > biggest.getCount()) {
        foundSlot = i;
        biggest = tmp;
      }
    }
    if (biggest.isEmpty()) {
      return;
    }
    //step 2: find all stacks that are allowed to merge with that. (keep track of both types of slots)
    Set<Integer> slotTargest = new HashSet<>();//including biggest
    int totalQuantity = 0;
    for (int i = 0; i <= 8; i++) {
      ItemStack tmp = c.getCraftMatrix().getStackInSlot(i);
      if (tmp.isEmpty()) {
        slotTargest.add(i);
      }
      else if (Container.areItemsAndTagsEqual(tmp, biggest)) {
        slotTargest.add(i);
        totalQuantity += tmp.getCount();
      }
    }
    ModCyclic.LOGGER.info(biggest.getItem() + "  totalQuantity" + totalQuantity);
    //step 3: extract all of those allowed to merge including original
    //step 4: flatten them out, with the remainder left over
    //ok. mow we can split it
    //
    //use myself plus all the empties
    int slotsUsedForBalancing = slotTargest.size();
    int avg = totalQuantity / slotsUsedForBalancing;
    int remainder = totalQuantity % slotsUsedForBalancing;
    ModCyclic.LOGGER.info("  slotsUsedForBalancing " + slotsUsedForBalancing);
    ModCyclic.LOGGER.info("  avg " + avg);
    ModCyclic.LOGGER.info("  remainder  " + remainder);
    if (avg == 0) {
      return;
    }
    for (int slot : slotTargest) {
      //remove everything and reset
      int size = (slot == foundSlot) ? avg + remainder : avg;
      c.getCraftMatrix().setInventorySlotContents(slot, new ItemStack(biggest.getItem(), size));
    }
    //    for (int j = 0; j <= 8; j++) {
    //      if (j != foundSlot) {
    //        ItemStack inMatrix = c.getCraftMatrix().getStackInSlot(j);
    //        //split 
    //        if (inMatrix.isEmpty())
    //          c.getCraftMatrix().setInventorySlotContents(j, biggest.split(avg));
    //        else if (Container.areItemsAndTagsEqual(inMatrix, biggest)) {
    //          ModCyclic.LOGGER.info(j + "merge into partial? " + inMatrix);
    //          int targetSpace = inMatrix.getMaxStackSize() - inMatrix.getCount();
    //          //does it have enough tho
    //          ItemStack mergeMe = biggest.split(Math.min(avg, targetSpace));
    //          c.getCraftMatrix().setInventorySlotContents(j, mergeMe);
    //          //
    //        }
    //      }
    //    }
  }
  //?v2?
}
