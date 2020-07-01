package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockFluidCollect extends BlockBase {

  public BlockFluidCollect(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFluidCollect();
  }
}
