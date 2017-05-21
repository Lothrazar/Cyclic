package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerUser extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 42;
  protected TileEntityUser tileEntity;
  private int tileSpeed;
  private int timer;
  private int redstone;
  private int leftright;
  private int tileSize;
  private int tileFuel;
  public ContainerUser(InventoryPlayer inventoryPlayer, TileEntityUser te) {
    tileEntity = te;
    this.setTile(te);
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      addSlotToContainer(new Slot(tileEntity, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
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
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
//    for (int i = 0; i < this.listeners.size(); ++i) {
//      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
//      int fieldId = TileEntityUser.Fields.SPEED.ordinal();
//      if (this.tileSpeed != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//      fieldId = TileEntityUser.Fields.TIMER.ordinal();
//      if (this.timer != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//      fieldId = TileEntityUser.Fields.REDSTONE.ordinal();
//      if (this.redstone != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//      fieldId = TileEntityUser.Fields.LEFTRIGHT.ordinal();
//      if (this.leftright != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//      fieldId = TileEntityUser.Fields.SIZE.ordinal();
//      if (this.tileSize != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//      fieldId = TileEntityUser.Fields.FUEL.ordinal();//TODO: shared fn in base class somehow!
//      if (this.tileFuel != this.tileEntity.getField(fieldId)) {
//        icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tileEntity.getField(fieldId));
//      }
//    }
//    this.tileSpeed = this.tileEntity.getField(TileEntityUser.Fields.SPEED.ordinal());
//    this.timer = this.tileEntity.getField(TileEntityUser.Fields.TIMER.ordinal());
//    this.redstone = this.tileEntity.getField(TileEntityUser.Fields.REDSTONE.ordinal());
//    this.leftright = this.tileEntity.getField(TileEntityUser.Fields.LEFTRIGHT.ordinal());
//    this.tileSize = this.tileEntity.getField(TileEntityUser.Fields.SIZE.ordinal());
//    this.tileFuel = this.tileEntity.getField(TileEntityUser.Fields.FUEL.ordinal());
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
