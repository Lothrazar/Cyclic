package com.lothrazar.cyclic.util;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public final class UtilItemHandler {
  private UtilItemHandler() {
  }

  public static IItemHandler get(final World world, final BlockPos blockPos, final Direction side) {
    return getOptCap(world, blockPos, side).resolve().orElse(null);
  }

  public static LazyOptional<IItemHandler> getOptCap(final World world, final BlockPos blockPos, final Direction side) {
    final TileEntity tileEntity = world.getTileEntity(blockPos);
    if (tileEntity != null) {
      return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
    }
    return LazyOptional.empty();
  }

  public static int moveItems(final IItemHandler input, final int inputSlot, final IItemHandler output, final int amount) {
    //first get the original ItemStack as creating new ones is expensive
    final ItemStack originalItemStack = input.getStackInSlot(inputSlot);
    if (originalItemStack.isEmpty()) {
      return 0;
    }

    final int originalCount = originalItemStack.getCount();
    int remainingItemCount = Math.min(originalCount, amount);
    if (remainingItemCount <= 0) {
      return 0;
    }

    ItemStack itemStackToInsert = null;
    for (int outputSlot = 0; outputSlot < output.getSlots(); outputSlot++) {
      int outputSlotLimit = output.getSlotLimit(outputSlot);

      final ItemStack stackInOutputSlot = output.getStackInSlot(outputSlot);
      if (!stackInOutputSlot.isEmpty()) {
        //if the output slot is not compatible then skip this slot
        if (!ItemHandlerHelper.canItemStacksStack(originalItemStack, stackInOutputSlot)) {
          continue;
        }

        outputSlotLimit -= stackInOutputSlot.getCount();
      }

      final int amountToExtract = Math.min(outputSlotLimit, remainingItemCount);
      if (amountToExtract <= 0) {
        continue;
      }

      //lazily create an ItemStack for inserting and re-use it for future inserts
      if (itemStackToInsert == null) {
        //use a simulated extraction due to buggy mods returning items different to those when calling getStackInSlot
        itemStackToInsert = input.extractItem(inputSlot, amountToExtract, true);

        //ItemStackHandlerWrapper will return amountToExtract of empty
        if (itemStackToInsert.isEmpty()) {
          return 0;
        }
      }
      else {
        itemStackToInsert.setCount(amountToExtract);
      }

      //only perform an insert (even simulated) if required as it creates at least one new ItemStack in the process
      final ItemStack remainderItemStack = output.insertItem(outputSlot, itemStackToInsert, false);

      //check the remainder to calculate how much was actually inserted
      remainingItemCount -= (amountToExtract - remainderItemStack.getCount());
      if (remainingItemCount <= 0) {
        break;
      }
    }

    final int insertedItemAmount = originalCount - remainingItemCount;
    if (insertedItemAmount > 0) {
      //only perform an extraction if required as it creates a new ItemStack in the process
      final ItemStack extractedItemStack = input.extractItem(inputSlot, insertedItemAmount, false);
    }

    return insertedItemAmount;
  }
}
