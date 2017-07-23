package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHydrator extends ContainerBaseMachine {
  static final int MID_SPACING = 133;
  static final int SLOTX_FLUID = 70;
  static final int SLOTY_FLUID = 39;
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  static final int SLOTX_START = 8;
  public static final int SLOTY = 30;
  static final int SQ = 18;
  public ContainerHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator te) {
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
    int slotNum = 0;
    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          SLOTX_START + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          MID_SPACING + 1 + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    addSlotToContainer(new Slot(tile, slotNum,
        SLOTX_FLUID,
        SLOTY_FLUID));
    slotNum++;
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
      if (slot < tile.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tile.getSizeInventory(), 36 + tile.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      else if (!this.mergeItemStack(stackInSlot, 0, 36, false)) { return ItemStack.EMPTY; }
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(ItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) { return ItemStack.EMPTY; }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tile.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tile);
  }
}
