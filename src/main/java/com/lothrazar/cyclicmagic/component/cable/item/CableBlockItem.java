package com.lothrazar.cyclicmagic.component.cable.item;

import com.lothrazar.cyclicmagic.component.cable.CableBlockPrimary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CableBlockItem extends CableBlockPrimary{

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return  new TileEntityItemCable();
  }
}
