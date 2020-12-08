package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.util.UtilEnchant;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EnderControllerItemHandler extends ItemStackHandler {

  private TileEnderShelf controller;

  public EnderControllerItemHandler(TileEnderShelf controller) {
    super(1);
    this.controller = controller;
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    ItemStack remaining = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount());
    remaining = insertItemElsewhere(remaining, false, simulate); //first try to put it in a matching slot
    if (!remaining.isEmpty())
      remaining = insertItemElsewhere(remaining, true, simulate); //then try to put it in the first open slot
    return remaining;
  }

  private ItemStack insertItemElsewhere(ItemStack stack, boolean insertWhenEmpty, boolean simulate) {
    for (BlockPos shelfPos : controller.getShelves()) {
      TileEntity te = controller.getWorld().getTileEntity(shelfPos);
      if (te != null && te.getBlockState().getBlock().getRegistryName().equals(EnderShelfHelper.ENDER_SHELF_REGISTRY_NAME)) {
        TileEnderShelf shelf = (TileEnderShelf) te;
        stack = insertItemElsewhere(shelf, stack, insertWhenEmpty, simulate);
      }
    }
    return stack;
  }

  private ItemStack insertItemElsewhere(TileEnderShelf shelf, ItemStack stack, boolean insertWhenEmpty, boolean simulate) {
    if (shelf.getBlockState().get(BlockEnderShelf.IS_CONTROLLER))
      return stack;
    EnderShelfItemHandler h = (EnderShelfItemHandler) shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
    List<Integer> emptySlots = new ArrayList<>();
    for (int i = 0; i < h.getSlots(); i++) {
      ItemStack slotStack = h.getStackInSlot(i);
      if (slotStack.isEmpty())
        emptySlots.add(i);
      else if (UtilEnchant.doBookEnchantmentsMatch(stack, slotStack) && slotStack.getCount() != h.getStackLimit(i, stack)) {
        return h.insertItem(i, stack, simulate);
      }
    }
    if (emptySlots.size() > 0 && insertWhenEmpty)
      return h.insertItem(emptySlots.get(0), stack, simulate);
    return stack;
  }
}
