package com.lothrazar.cyclicmagic.component.miner;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlockMiner extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  public static final int SQ = 18;
  protected TileEntityBlockMiner tileEntity;
  public ContainerBlockMiner(InventoryPlayer inventoryPlayer, TileEntityBlockMiner te) {

    this.setTile(te);
    tileEntity = te;
    bindPlayerInventory(inventoryPlayer);
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
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return ItemStack.EMPTY;
  }
}
