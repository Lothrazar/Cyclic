package com.lothrazar.cyclicmagic.block.anvilvoid;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVoidAnvil extends ContainerBaseMachine {

  public static final int SLOTY = 34;
  public ContainerVoidAnvil(InventoryPlayer inventoryPlayer, TileEntityVoidAnvil te) {
    super(te);
    //    this.setScreenSize(ScreenSize.LARGE);
    this.addSlotToContainer(new Slot(tile, 0, 50, SLOTY));
    this.addSlotToContainer(new Slot(tile, 1, 110, SLOTY));
    bindPlayerInventory(inventoryPlayer);
  }

  // places it into the tileEntity is possible since its in the player inventory
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
