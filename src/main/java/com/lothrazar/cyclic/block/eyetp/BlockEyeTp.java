package com.lothrazar.cyclic.block.eyetp;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockEyeTp extends BlockBase {

  public BlockEyeTp(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEyeTp();
  }
}
