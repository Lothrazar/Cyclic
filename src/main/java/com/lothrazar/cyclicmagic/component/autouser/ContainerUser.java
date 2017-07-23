package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerUser extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 94;
  public ContainerUser(InventoryPlayer inventoryPlayer, TileEntityUser te) {
    this.screenSize = ScreenSize.LARGE;
    this.setTile(te);
    for (int i = 0; i < 3; i++) {//0 1 2 
      addSlotToContainer(new Slot(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    for (int i = 3; i < 6; i++) {//345
      addSlotToContainer(new SlotOutputOnly(tile, i, SLOTX_START + (i + 3) * Const.SQ, SLOTY));
    }
    for (int i = 6; i < 9; i++) {//678
      addSlotToContainer(new SlotOutputOnly(tile, i, SLOTX_START + i * Const.SQ, SLOTY - Const.SQ));
    }
    addSlotToContainer(new SlotFurnaceFuel(tile, tile.getSizeInventory() - 1, SLOTX_FUEL, SLOTY_FUEL));//fuel slot==9
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
      if (slot < tile.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tile.getSizeInventory(), 36 + tile.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      else if (TileEntityFurnace.isItemFuel(stack)) {
        //fuel slot
        if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory(), true)) { return ItemStack.EMPTY; }
        //        else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory()-1, false)) { return ItemStack.EMPTY; }
      }
      else if (!this.mergeItemStack(stackInSlot, 0, tile.getSizeInventory(), false)) { return ItemStack.EMPTY; }
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
