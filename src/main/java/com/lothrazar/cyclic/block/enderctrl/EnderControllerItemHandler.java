package com.lothrazar.cyclic.block.enderctrl;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.util.UtilEnchant;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class EnderControllerItemHandler extends ItemStackHandler {

  //each shelf has 5 rows
  static final int SLOTS_PER_SHELF = 5;
  private final TileEnderCtrl controller;

  public EnderControllerItemHandler(TileEnderCtrl controller) {
    super(1);
    this.controller = controller;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack remaining, boolean simulate) {
    if (remaining.getItem() != Items.ENCHANTED_BOOK) {
      return remaining;
    }
    //    ItemStack remaining = stack; //ItemHandlerHelper.copyStackWithSize(stack, stack.getCount());
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
        try {
          stack = insertItemActual(shelf, stack, insertWhenEmpty, simulate);
        }
        catch (Exception e) {
          ModCyclic.LOGGER.error("Insert item shelf error", e);
        }
      }
    }
    return stack;
  }

  private ItemStack insertItemActual(TileEnderShelf shelf, ItemStack stack, boolean insertWhenEmpty, boolean simulate) {
    List<Integer> emptySlots = new ArrayList<>();
    final int slots = shelf.inventory.getSlots();
    for (int i = 0; i < slots; i++) {
      ItemStack slotStack = shelf.inventory.getStackInSlot(i);
      if (slotStack.isEmpty()) {
        emptySlots.add(i);
      }
      else if (slotStack.getCount() < shelf.inventory.getStackLimit(i, stack) && UtilEnchant.doBookEnchantmentsMatch(stack, slotStack)) {
        return shelf.inventory.insertItem(i, stack, simulate);
      }
    }
    if (emptySlots.size() > 0 && insertWhenEmpty) {
      ItemStack inserted = shelf.inventory.insertItem(emptySlots.get(0), stack, simulate);
      if (inserted.isEmpty()) {
        return inserted;
      }
    }
    return stack;
  }

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
