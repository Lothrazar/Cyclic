package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotOnlyBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBuilder extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 52;
  public static final int SQ = 18;
  protected TileEntityStructureBuilder tileEntity;
  private int tileBuild;
  private int tileTimer;
  private int tileSpeed;
  private int tileSize;
  private int tileHeight;
  private int tileRedstone;
  private int tileToggle;
  public ContainerBuilder(InventoryPlayer inventoryPlayer, TileEntityStructureBuilder te) {
    tileEntity = te;
    this.setTile(te);
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      addSlotToContainer(new SlotOnlyBlocks(tileEntity, i, SLOTX_START + i * SQ, SLOTY));
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
      int idx = TileEntityStructureBuilder.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.BUILDTYPE.ordinal();
      if (this.tileBuild != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.SIZE.ordinal();
      if (this.tileSize != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.SPEED.ordinal();
      if (this.tileSpeed != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.HEIGHT.ordinal();
      if (this.tileHeight != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal();
      if (this.tileToggle != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileEntityStructureBuilder.Fields.TIMER.ordinal());
    this.tileBuild = this.tileEntity.getField(TileEntityStructureBuilder.Fields.BUILDTYPE.ordinal());
    this.tileSize = this.tileEntity.getField(TileEntityStructureBuilder.Fields.SIZE.ordinal());
    this.tileSpeed = this.tileEntity.getField(TileEntityStructureBuilder.Fields.SPEED.ordinal());
    this.tileHeight = this.tileEntity.getField(TileEntityStructureBuilder.Fields.HEIGHT.ordinal());
    this.tileRedstone = this.tileEntity.getField(TileEntityStructureBuilder.Fields.REDSTONE.ordinal());
    this.tileToggle = this.tileEntity.getField(TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal());
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
