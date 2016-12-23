package com.lothrazar.cyclicmagic.gui.pattern;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPattern extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  
  public static final int SLOTX_START = 8;
  public static final int SLOTY_START = 90;
  protected TileEntityPatternBuilder tileEntity;
  private int tileOX;
  private int tileOY;
  private int tileOZ;
  private int tileSOX;
  private int tileSOY;
  private int tileSOZ;
  private int tileSIZER;
  private int tileHEIGHT;
  private int tileREDSTONE;
  public ContainerPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder te) {
    this.playerOffsetY = 130;
    tileEntity = te;
    int s = 0;
    int row = 0, col = 0;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) { //so going from 0-9
      row = i / GuiPattern.GUI_ROWS;// /3 will go 000, 111, 222
      col = i % GuiPattern.GUI_ROWS; // and %3 will go 012 012 012
      addSlotToContainer(new Slot(tileEntity, s, SLOTX_START + row * Const.SQ, SLOTY_START + col * Const.SQ));
      s++;
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
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
      // places it into the tileEntity is possible since its in the player inventory
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
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityPatternBuilder.Fields.OFFTARGX.ordinal();
      if (this.tileOX != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.OFFTARGY.ordinal();
      if (this.tileOY != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.OFFTARGZ.ordinal();
      if (this.tileOZ != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.OFFSRCX.ordinal();
      if (this.tileSOX != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.OFFSRCY.ordinal();
      if (this.tileSOY != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.OFFSRCZ.ordinal();
      if (this.tileSOZ != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.SIZER.ordinal();
      if (this.tileSIZER != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.HEIGHT.ordinal();
      if (this.tileHEIGHT != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityPatternBuilder.Fields.REDSTONE.ordinal();
      if (this.tileREDSTONE != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileOX = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFTARGX);
    this.tileOY = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFTARGY);
    this.tileOZ = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFTARGZ);
    this.tileSOX = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFSRCX);
    this.tileSOY = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFSRCY);
    this.tileSOZ = this.tileEntity.getField(TileEntityPatternBuilder.Fields.OFFSRCZ);
    this.tileSIZER = this.tileEntity.getField(TileEntityPatternBuilder.Fields.SIZER);
    this.tileHEIGHT = this.tileEntity.getField(TileEntityPatternBuilder.Fields.HEIGHT);
    this.tileREDSTONE = this.tileEntity.getField(TileEntityPatternBuilder.Fields.REDSTONE);
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
