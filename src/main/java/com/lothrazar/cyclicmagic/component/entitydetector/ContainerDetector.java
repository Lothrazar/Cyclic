package com.lothrazar.cyclicmagic.component.entitydetector;
import com.lothrazar.cyclicmagic.component.entitydetector.TileEntityDetector.Fields;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDetector extends ContainerBaseMachine {
  public static final int SLOTX_START = 8;
  public static final int SLOTY_START = 90;
  protected TileEntityDetector tileEntity;
  private int tileOX;
  private int tileOY;
  private int tileOZ;
  private int tileLimit;
  private int tileGreaterThan;
  private int tileEntityType;
  public ContainerDetector(InventoryPlayer inventoryPlayer, TileEntityDetector te) {
    tileEntity = te;
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = Fields.RANGEX.ordinal();
      if (this.tileOX != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.RANGEY.ordinal();
      if (this.tileOY != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.RANGEZ.ordinal();
      if (this.tileOZ != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.LIMIT.ordinal();
      if (this.tileLimit != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.ENTITYTYPE.ordinal();
      if (this.tileEntityType != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.GREATERTHAN.ordinal();
      if (this.tileGreaterThan != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileOX = this.tileEntity.getField(Fields.RANGEX);
    this.tileOY = this.tileEntity.getField(Fields.RANGEY);
    this.tileOZ = this.tileEntity.getField(Fields.RANGEZ);
    this.tileLimit = this.tileEntity.getField(Fields.LIMIT);
    this.tileEntityType = this.tileEntity.getField(Fields.ENTITYTYPE);
    this.tileGreaterThan = this.tileEntity.getField(Fields.GREATERTHAN);
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
