package com.lothrazar.cyclicmagic.component.anvil;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAnvilAuto extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX = 50;
  public static final int SLOTY = 34;
  protected TileEntityAnvilAuto tileEntity;
  public ContainerAnvilAuto(InventoryPlayer inventoryPlayer, TileEntityAnvilAuto te) {
    //    this.screenSize = ScreenSize.LARGE;
    tileEntity = te;
    this.setTile(te);
    this.addSlotToContainer(new Slot(tileEntity, 0, 60, SLOTY));
    this.addSlotToContainer(new Slot(tileEntity, 1, 110, SLOTY));
    super.addFurnaceFuelSlot(SLOTX_FUEL, SLOTY_FUEL);
    this.bindPlayerInventory(inventoryPlayer);
  }
  // places it into the tileEntity is possible since its in the player inventory
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
