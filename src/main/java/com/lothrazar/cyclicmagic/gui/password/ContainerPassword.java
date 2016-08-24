package com.lothrazar.cyclicmagic.gui.password;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPassword extends Container {
  TileEntityPassword tile;
  public ContainerPassword(TileEntityPassword te) {
    super();
    tile = te;
  }
  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
  }
}
