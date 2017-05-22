package com.lothrazar.cyclicmagic.component.fan;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFan extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  public static final int SQ = 18;
  protected TileEntityFan tileEntity;
  private int tileRedstone;
  private int tileTimer;
  private int tileSize;
  private int tilePush;
  public ContainerFan(InventoryPlayer inventoryPlayer, TileEntityFan te) {
    tileEntity = te;
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return ItemStack.EMPTY;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityFan.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityFan.Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityFan.Fields.PARTICLES.ordinal();
      if (this.tileSize != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityFan.Fields.PUSHPULL.ordinal();
      if (this.tilePush != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileEntityFan.Fields.TIMER.ordinal());
    this.tileRedstone = this.tileEntity.getField(TileEntityFan.Fields.REDSTONE.ordinal());
    this.tileSize = this.tileEntity.getField(TileEntityFan.Fields.PARTICLES.ordinal());
    this.tilePush = this.tileEntity.getField(TileEntityFan.Fields.PUSHPULL.ordinal());
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
