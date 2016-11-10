package com.lothrazar.cyclicmagic.gui.harvester;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHarvester extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  public static final int SQ = 18;
  protected TileMachineHarvester tileEntity;
  private int tileRedstone;
  private int tileTimer;
  public ContainerHarvester(InventoryPlayer inventoryPlayer, TileMachineHarvester te) {
    tileEntity = te;
//    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
//      addSlotToContainer(new SlotOnlyBlocks(tileEntity, i, SLOTX_START + i * SQ, SLOTY));
//    }
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = null;
//    Slot slotObject = (Slot) inventorySlots.get(slot);
//    // null checks and checks if the item can be stacked (maxStackSize > 1)
////    if (slotObject != null && slotObject.getHasStack()) {
//      ItemStack stackInSlot = slotObject.getStack();
//      stack = stackInSlot.copy();
//      // merges the item into player inventory since its in the tileEntity
//      if (slot < tileEntity.getSizeInventory()) {
//        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return null; }
//      }
//      // places it into the tileEntity is possible since its in the player
//      // inventory
//      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return null; }
//      if (stackInSlot.stackSize == 0) {
//        slotObject.putStack(null);
//      }
//      else {
//        slotObject.onSlotChanged();
//      }
//      if (stackInSlot.stackSize == stack.stackSize) { return null; }
//      slotObject.onPickupFromSlot(player, stackInSlot);
//    }
    return stack;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileMachineHarvester.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileMachineHarvester.Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileMachineHarvester.Fields.TIMER.ordinal());
    this.tileRedstone = this.tileEntity.getField(TileMachineHarvester.Fields.REDSTONE.ordinal());
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
