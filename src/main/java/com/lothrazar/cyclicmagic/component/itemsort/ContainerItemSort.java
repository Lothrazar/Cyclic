package com.lothrazar.cyclicmagic.component.itemsort;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotSingleStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerItemSort extends ContainerBaseMachine {
  public static final int SLOTX_START = Const.PAD;
  public static final int SLOTY = Const.SQ;
  public ContainerItemSort(InventoryPlayer inventoryPlayer, TileEntityItemCableSort te) {
    this.screenSize = ScreenSize.LARGE;
    this.setTile(te);
    int fs = TileEntityItemCableSort.FILTER_SIZE;
    int slot = 1;
    int y = SLOTY;
    for (int col = 0; col < EnumFacing.values().length; col++) {
      y = col * Const.SQ;
      for (int row = 0; row < fs; row++) {
        addSlotToContainer(new SlotSingleStack(tile, slot,
            SLOTX_START + row % fs * Const.SQ + Const.SQ,
            SLOTY + y));
        slot++;
      }
    }
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ModCyclic.logger.log("!!" + slot);
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tile.getSizeInventory() - 1) {
        if (!this.mergeItemStack(stackInSlot, tile.getSizeInventory() - 1, 36 + tile.getSizeInventory() - 1, true)) {
          return ItemStack.EMPTY;
        }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory() - 1, false)) {
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
