package com.lothrazar.cyclicmagic.gui.password;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPassword extends Container {

  public ContainerPassword() {
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }
}
