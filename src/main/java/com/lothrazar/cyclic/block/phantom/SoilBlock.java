package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class SoilBlock extends BlockBase {

  public SoilBlock(Properties properties) {
    super(properties.hardnessAndResistance(1));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new SoilTile();
  }
}
