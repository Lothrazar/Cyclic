package com.lothrazar.cyclicmagic.component.cable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CableBlockFluid extends CableBlockPrimary{

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return  new CableTileFluid();
  }
}
