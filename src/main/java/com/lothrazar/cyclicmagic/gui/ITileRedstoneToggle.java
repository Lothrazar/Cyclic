package com.lothrazar.cyclicmagic.gui;
import net.minecraft.util.EnumFacing;

public interface ITileRedstoneToggle {
  public void toggleNeedsRedstone();
  public boolean onlyRunIfPowered();
 
}
