package com.lothrazar.cyclicmagic.gui.uncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerUncrafting extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 10;
  public static final int SLOTY = 28;
  protected TileMachineUncrafter tileEntity;
  public ContainerUncrafting(InventoryPlayer inventoryPlayer, TileMachineUncrafter te) {
    tileEntity = te;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      addSlotToContainer(new Slot(tileEntity, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = null;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return null; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return null; }
      if (stackInSlot.stackSize == 0) {
        slotObject.putStack(null);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.stackSize == stack.stackSize) { return null; }
      slotObject.onPickupFromSlot(player, stackInSlot);
    }
    return stack;
  }
}
