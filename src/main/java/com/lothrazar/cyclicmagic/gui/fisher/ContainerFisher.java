package com.lothrazar.cyclicmagic.gui.fisher;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFisher extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 10;
  public static final int SLOTY = 28;
  public static final int SLOTX_FISH = 99;
  public static final int SLOTY_FISH = 20;
  protected TileEntityFishing tileEntity;
  public ContainerFisher(InventoryPlayer inventoryPlayer, TileEntityFishing te) {
    tileEntity = te;
    for (int i = 0; i < TileEntityFishing.BAITSLOTS; i++) {
      addSlotToContainer(new Slot(tileEntity, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
     
    int s = TileEntityFishing.BAITSLOTS;
    int row = 0, col = 0;
    for (int i = 0; i < TileEntityFishing.FISHSLOTS; i++) {//so going from 0-9
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3;  // and %3 will go 012 012 012
      addSlotToContainer(new Slot(tileEntity, s, SLOTX_FISH + row * Const.SQ, SLOTY_FISH + col * Const.SQ));
      s++;
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
