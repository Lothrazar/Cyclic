package com.lothrazar.cyclicmagic.component.peat.farm;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotCheckTileValid;
import com.lothrazar.cyclicmagic.gui.slot.SlotItemRestricted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPeatFarm extends ContainerBaseMachine {
  static final int SLOTY_FLUID = 39;
  static final int SLOTX_START = 8;
  static final int MID_SPACING = 52;
  public static final int SLOTY = 36;
  public ContainerPeatFarm(InventoryPlayer inventoryPlayer, TileEntityPeatFarm te) {
    this.setTile(te);
    int slotNum = 0;
    int rowSize = 6;
    for (int i = 0; i < rowSize; i++) {
      addSlotToContainer(new SlotCheckTileValid(tile, slotNum,
          SLOTX_START + i  * Const.SQ,
          SLOTY  ));
      slotNum++;
    }
    for (int i = rowSize; i < 2 * rowSize; i++) {
      addSlotToContainer(new SlotCheckTileValid(tile, slotNum,
          SLOTX_START + (i - rowSize) * Const.SQ,
          SLOTY +   Const.SQ));
      slotNum++;
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
      // places it into the tileEntity is possible since its in the player
      // inventory
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
