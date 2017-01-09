package com.lothrazar.cyclicmagic.gui.vector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector.Fields;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerVector extends ContainerBaseMachine {
  protected TileVector tileEntity;
  private int tileAngle;
  private int tilePower;
  private int tileYaw;
  private int tileSound;
  private int tileRedstone;
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
      idx = Fields.YAW.ordinal();
      if (this.tileYaw != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.SOUND.ordinal();
      if (this.tileSound != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileAngle = this.tileEntity.getField(Fields.ANGLE.ordinal());
    this.tilePower = this.tileEntity.getField(Fields.POWER.ordinal());
    this.tileYaw = this.tileEntity.getField(Fields.YAW.ordinal());
    this.tileSound = this.tileEntity.getField(Fields.SOUND.ordinal());
    this.tileRedstone = this.tileEntity.getField(Fields.REDSTONE.ordinal());
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
