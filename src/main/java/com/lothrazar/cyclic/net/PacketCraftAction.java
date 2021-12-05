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
import com.lothrazar.cyclic.data.CraftingActionEnum;
import com.lothrazar.cyclic.data.IContainerCraftingAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketCraftAction extends PacketBaseCyclic {

  private CraftingActionEnum action;

  public PacketCraftAction(CraftingActionEnum s) {
    action = s;
  }

  public static PacketCraftAction decode(FriendlyByteBuf buf) {
    return new PacketCraftAction(CraftingActionEnum.values()[buf.readInt()]);
  }

  public static void encode(PacketCraftAction msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.action.ordinal());
  }

  public static void handle(PacketCraftAction message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      ServerPlayer sender = ctx.get().getSender();
      if (sender.containerMenu instanceof IContainerCraftingAction) {
        //do the thing
        IContainerCraftingAction c = (IContainerCraftingAction) sender.containerMenu;
        performAction(c, sender, message.action);
      }
    });
    message.done(ctx);
  }

  private static void performAction(IContainerCraftingAction c, Player player, CraftingActionEnum action) {
    //call this serverside from a packet
    switch (action) {
      case EMPTY:
        // move all slots down out of grid into inventory
        for (int i = 1; i <= 9; i++) {
          c.transferStack(player, i);
        }
        c.getCraftResult().clearContent();
      break;
      case SPREAD:
        balanceLargestSlot(c, false);
      break;
      case SPREADMATCH:
        balanceLargestSlot(c, true);
      break;
    }
  }

  private static void balanceLargestSlot(IContainerCraftingAction c, boolean onlyExisting) {
    //step 1: find the stack with the largest size.
    ItemStack biggest = ItemStack.EMPTY;
    int foundSlot = -1;
    for (int i = 0; i <= 8; i++) {
      ItemStack tmp = c.getCraftMatrix().getItem(i);
      if (!tmp.isEmpty() && tmp.getCount() > biggest.getCount()) {
        foundSlot = i;
        biggest = tmp;
      }
    }
    if (biggest.isEmpty()) {
      return;
    }
    //step 2: find all stacks that are allowed to merge with that. (keep track of both types of slots)
    Set<Integer> slotTargest = new HashSet<>();
    int totalQuantity = 0;
    for (int i = 0; i <= 8; i++) {
      ItemStack tmp = c.getCraftMatrix().getItem(i);
      if (tmp.isEmpty() && !onlyExisting) {
        slotTargest.add(i);
      }
      if (ItemStack.isSameItemSameTags(tmp, biggest)) { // AbstractContainerMenu.consideredTheSameItem(tmp, biggest)) {
        slotTargest.add(i);
        totalQuantity += tmp.getCount();
      }
    }
    //step 3: extract all of those allowed to merge including original
    //step 4: flatten them out, with the remainder left over
    //ok. mow we can split it
    //
    //use myself plus all the empties
    int slotsUsedForBalancing = slotTargest.size();
    int avg = totalQuantity / slotsUsedForBalancing;
    int remainder = totalQuantity % slotsUsedForBalancing;
    if (avg == 0) {
      return;
    }
    for (int slot : slotTargest) {
      //remove everything and reset
      int size = (slot == foundSlot) ? avg + remainder : avg;
      ItemStack copy = biggest.copy();
      copy.setCount(size);
      c.getCraftMatrix().setItem(slot, copy);
    }
  }
}
