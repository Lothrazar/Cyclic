package com.lothrazar.cyclicmagic.component.dropper;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDropperExact extends ContainerBaseMachine {
  public static final int SLOTX_START = 104;
  public static final int SLOTY = 20;
  public ContainerDropperExact(InventoryPlayer inventoryPlayer, TileEntityDropperExact tileEntity) {
    super(tileEntity);
    int rows = 3, cols = 3, slot = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new Slot(tile, slot,
            SLOTX_START + j * Const.SQ,
            SLOTY + i * Const.SQ));
        slot++;
      }
    }
    bindPlayerInventory(inventoryPlayer);
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
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    //getSizeInventory is only 9 though, because output stack is not part of the size
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      if (slot < 9) {
        if (!this.mergeItemStack(stackInSlot, 9, 45, true)) {
          return ItemStack.EMPTY;
        }
      }
      //Move up into crafting grid
      else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
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
