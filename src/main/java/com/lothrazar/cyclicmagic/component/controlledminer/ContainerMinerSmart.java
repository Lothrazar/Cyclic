package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotSingleStack;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMinerSmart extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = Const.PAD;
  public static final int SLOTY = 88;
  public static final int SLOTID_EQUIP = 4;
  public static final int SLOTEQUIP_X = SLOTX_START + (SLOTID_EQUIP + 2) * Const.SQ;
  public static final int SLOTEQUIP_Y = SLOTY;
  protected TileEntityControlledMiner tileEntity;
  public ContainerMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner te) {
    tileEntity = te;
    this.screenSize = ScreenSize.LARGE;
    this.setTile(te);
    for (int i = 0; i < tileEntity.getSizeInventory() - 1; i++) {
      addSlotToContainer(new SlotSingleStack(tileEntity, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    addSlotToContainer(new SlotSingleStack(tileEntity, SLOTID_EQUIP, SLOTEQUIP_X, SLOTEQUIP_Y));
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
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return ItemStack.EMPTY; }
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
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
