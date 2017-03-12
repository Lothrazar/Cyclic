package com.lothrazar.cyclicmagic.gui.pylon;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPylon extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  public static final int SQ = 18;
  protected TileEntityXpPylon tileEntity;
  private int tileRedstone;
  private int tileTimer;
  private int tileXp;
  private int tileMODE;
  public ContainerPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon te) {
    tileEntity = te;
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return null;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityXpPylon.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.REDSTONE.ordinal();
      if (this.tileRedstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.EXP.ordinal();
      if (this.tileXp != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.MODE.ordinal();
      if (this.tileMODE != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileEntityXpPylon.Fields.TIMER.ordinal());
    this.tileRedstone = this.tileEntity.getField(TileEntityXpPylon.Fields.REDSTONE.ordinal());
    this.tileXp = this.tileEntity.getField(TileEntityXpPylon.Fields.EXP.ordinal());
    this.tileMODE = this.tileEntity.getField(TileEntityXpPylon.Fields.MODE.ordinal());
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
