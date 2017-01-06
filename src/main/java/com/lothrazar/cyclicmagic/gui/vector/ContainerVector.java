package com.lothrazar.cyclicmagic.gui.vector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector.Fields;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerVector extends ContainerBaseMachine {
  public static final int SLOTX_START = 8;
  public static final int SLOTY_START = 90;
  protected TileVector tileEntity;
  private int tileAngle;
  private int tilePower;
  public ContainerVector(InventoryPlayer inventoryPlayer, TileVector te) {
    tileEntity = te;
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = Fields.ANGLE.ordinal();
      if (this.tileAngle != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.POWER.ordinal();
      if (this.tilePower != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
//      idx = Fields.RANGEZ.ordinal();
//      if (this.tileOZ != this.tileEntity.getField(idx)) {
//        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
//      }
//      idx = Fields.LIMIT.ordinal();
//      if (this.tileLimit != this.tileEntity.getField(idx)) {
//        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
//      }
//      idx = Fields.ENTITYTYPE.ordinal();
//      if (this.tileEntityType != this.tileEntity.getField(idx)) {
//        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
//      }
//      idx = Fields.GREATERTHAN.ordinal();
//      if (this.tileGreaterThan != this.tileEntity.getField(idx)) {
//        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
//      }
    }
    this.tileAngle = this.tileEntity.getField(Fields.ANGLE.ordinal());
    this.tilePower = this.tileEntity.getField(Fields.POWER.ordinal());
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
