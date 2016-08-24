package com.lothrazar.cyclicmagic.gui.password;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerPassword extends Container {

  public static final int SLOTX_START = 10;
  public static final int SLOTY = 28;

  public static final int SQ = 18;

  protected TileEntityPassword tileEntity;
  public ContainerPassword(InventoryPlayer inventoryPlayer, TileEntityPassword te) {
    tileEntity = te;
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }
}
