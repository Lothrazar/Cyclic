package com.lothrazar.cyclic.block.soil;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockTerraPreta extends BlockBase {

  public BlockTerraPreta(Properties properties) {
    //https://en.wikipedia.org/wiki/Terra_preta
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTerraPreta();
  }
}
