package com.lothrazar.cyclicmagic.component.fisher;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotCheckTileValid;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFisher extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 30;
  public static final int SLOTY = 20;
  public static final int SLOTX_FISH = 80;
  public static final int SLOTY_FISH = 20;
  protected TileEntityFishing tileEntity;
  public ContainerFisher(InventoryPlayer inventoryPlayer, TileEntityFishing te) {
    tileEntity = te;
    this.setTile(te);
    //the first slot is the rod
    addSlotToContainer(new SlotCheckTileValid(tileEntity, TileEntityFishing.SLOT_TOOL, SLOTX_START, SLOTY));
    int s = 1;
    int row = 0, col = 0;
    for (int i = 0; i < TileEntityFishing.FISHSLOTS; i++) { //so going from 0-9
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      addSlotToContainer(new SlotOutputOnly(tileEntity, s, SLOTX_FISH + row * Const.SQ, SLOTY_FISH + col * Const.SQ));
      s++;
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) {
          return ItemStack.EMPTY;
        }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) {
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
