package com.lothrazar.cyclicmagic.block.base;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockHasTESR {
  @SideOnly(Side.CLIENT)
  public void initModel();
}
