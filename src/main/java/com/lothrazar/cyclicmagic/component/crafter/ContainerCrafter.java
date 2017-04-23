package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotOutputOnly;
import com.lothrazar.cyclicmagic.gui.SlotSingleStack;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrafter extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  protected TileEntityCrafter tileEntity;
  private int tileRedstone;
  private int tileTimer;
  public ContainerCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter te) {
    tileEntity = te;
    int slot = 0;
    //inpt on left
    int xPrefix = Const.padding, yPrefix = 16, rows = 3, cols = 2;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new Slot(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    //crafting in the middle
    rows = cols = 3;
    xPrefix = 58;
//    yPrefix = 20;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new SlotSingleStack(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    //output on right
    xPrefix = 128;
    rows = 3;
    cols = 2;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new SlotOutputOnly(tileEntity, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
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
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityCrafter.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityCrafter.Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileEntityCrafter.Fields.TIMER.ordinal());
    this.tileRedstone = this.tileEntity.getField(TileEntityCrafter.Fields.REDSTONE.ordinal());
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
