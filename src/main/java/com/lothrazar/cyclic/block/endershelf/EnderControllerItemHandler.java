package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.util.UtilEnchant;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class EnderControllerItemHandler extends ItemStackHandler {

  private final TileEnderShelf controller;

  public EnderControllerItemHandler(TileEnderShelf controller) {
    super(1);
    this.controller = controller;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    ItemStack remaining = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount());
    remaining = insertItemElsewhere(remaining, false, simulate); //first try to put it in a matching slot
    if (!remaining.isEmpty()) {
      remaining = insertItemElsewhere(remaining, true, simulate); //then try to put it in the first open slot
    }
    return remaining;
  }

  private ItemStack insertItemElsewhere(ItemStack stack, boolean insertWhenEmpty, boolean simulate) {
    if (controller.getWorld() == null) {
      return stack;
    }
    for (BlockPos shelfPos : controller.getShelves()) {
      TileEntity te = controller.getWorld().getTileEntity(shelfPos);
      if (te != null && EnderShelfHelper.isShelf(te.getBlockState())) {
        TileEnderShelf shelf = (TileEnderShelf) te;
        stack = insertItemElsewhere(shelf, stack, insertWhenEmpty, simulate);
      }
    }
    return stack;
  }

  private ItemStack insertItemElsewhere(TileEnderShelf shelf, ItemStack stack, boolean insertWhenEmpty, boolean simulate) {
    if (EnderShelfHelper.isController(shelf.getBlockState())) {
      return stack;
    }
    EnderShelfItemHandler h = EnderShelfHelper.getShelfHandler(shelf);
    if (h == null) {
      return stack;
    }
    List<Integer> emptySlots = new ArrayList<>();
    for (int i = 0; i < h.getSlots(); i++) {
      ItemStack slotStack = h.getStackInSlot(i);
      if (slotStack.isEmpty()) {
        emptySlots.add(i);
      }
      else if (UtilEnchant.doBookEnchantmentsMatch(stack, slotStack) && slotStack.getCount() != h.getStackLimit(i, stack)) {
        return h.insertItem(i, stack, simulate);
      }
    }
    if (emptySlots.size() > 0 && insertWhenEmpty) {
      return h.insertItem(emptySlots.get(0), stack, simulate);
    }
    return stack;
  }

  //each shelf has 5 rows
  static final int SLOTS_PER_SHELF = 5;

  @Override
  public int getSlots() {
    return this.controller.getShelves().size() * 5;
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    this.insertItem(slot, stack, false);
    onContentsChanged(slot);
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    if (this.controller.getShelves().size() == 0 || this.controller.getWorld() == null) {
      return ItemStack.EMPTY;
    }
    int shelf = slot / SLOTS_PER_SHELF;
    int realSlot = slot % SLOTS_PER_SHELF;
    //
    EnderShelfItemHandler handler = getHandlerAt(shelf);
    if (handler != null) {
      return handler.getStackInSlot(realSlot);
    }
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    return extractItemElsewhere(slot, amount, simulate);
  }

  private ItemStack extractItemElsewhere(int slot, int amount, boolean simulate) {
    if (this.controller.getShelves().size() == 0 || this.controller.getWorld() == null) {
      return ItemStack.EMPTY;
    }
    int shelf = slot / SLOTS_PER_SHELF;
    int realSlot = slot % SLOTS_PER_SHELF;
    // 
    EnderShelfItemHandler handler = getHandlerAt(shelf);
    if (handler != null) {
      return handler.extractItem(realSlot, amount, simulate);
    }
    return ItemStack.EMPTY;
  }

  /**
   * null if not found
   */
  private EnderShelfItemHandler getHandlerAt(int shelf) {
    try {
      BlockPos extractPos = this.controller.getShelves().get(shelf);
      TileEntity te = this.controller.getWorld().getTileEntity(extractPos);
      return EnderShelfHelper.getShelfHandler(te);
    }
    catch (Exception e) {
      return null; // index OOB, etc 
    }
  }
}
