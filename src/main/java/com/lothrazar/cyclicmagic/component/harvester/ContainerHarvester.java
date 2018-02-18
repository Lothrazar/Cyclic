package com.lothrazar.cyclicmagic.component.harvester;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHarvester extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_FUEL = 8 * Const.SQ + Const.PAD;
  public static final int SLOTY_FUEL = Const.PAD;
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 58;
  public static final int SQ = 18;
  public ContainerHarvester(InventoryPlayer inventoryPlayer, TileEntityHarvester te) {
    this.setTile(te);
    this.screenSize = ScreenSize.LARGE;
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    for (int i = 9; i < 18; i++) {
      addSlotToContainer(new Slot(tile, i, SLOTX_START + (i - 9) * Const.SQ, SLOTY + Const.SQ));
    }
    for (int i = 18; i < 27; i++) {
      addSlotToContainer(new Slot(tile, i, SLOTX_START + (i - 18) * Const.SQ, SLOTY + 2 * Const.SQ));
    }
    super.addFurnaceFuelSlot(SLOTX_FUEL, SLOTY_FUEL);
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
