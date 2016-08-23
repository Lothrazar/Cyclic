package com.lothrazar.cyclicmagic.gui.builder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.slot.SlotUncraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class ContainerBuilder extends Container {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 52;
  public static final int SQ = 18;
  protected TileMachineBuilder tileEntity;
  private int tileBuild;
  private int tileTimer;
  private int tileSpeed;
  private int tileSize;
  private int tileHeight;
  public ContainerBuilder(InventoryPlayer inventoryPlayer, TileMachineBuilder te) {
    tileEntity = te;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      addSlotToContainer(new SlotUncraft(tileEntity, i, SLOTX_START + i * SQ, SLOTY));
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    }
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = null;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return null; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return null; }
      if (stackInSlot.stackSize == 0) {
        slotObject.putStack(null);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.stackSize == stack.stackSize) { return null; }
      slotObject.onPickupFromSlot(player, stackInSlot);
    }
    return stack;
  }
  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileMachineBuilder.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileMachineBuilder.Fields.BUILDTYPE.ordinal();
      if (this.tileBuild != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileMachineBuilder.Fields.SIZE.ordinal();
      if (this.tileSize != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileMachineBuilder.Fields.SPEED.ordinal();
      if (this.tileSpeed != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileMachineBuilder.Fields.HEIGHT.ordinal();
      if (this.tileHeight != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileMachineBuilder.Fields.TIMER.ordinal());
    this.tileBuild = this.tileEntity.getField(TileMachineBuilder.Fields.BUILDTYPE.ordinal());
    this.tileSize = this.tileEntity.getField(TileMachineBuilder.Fields.SIZE.ordinal());
    this.tileSpeed = this.tileEntity.getField(TileMachineBuilder.Fields.SPEED.ordinal());
    this.tileHeight = this.tileEntity.getField(TileMachineBuilder.Fields.HEIGHT.ordinal());
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
