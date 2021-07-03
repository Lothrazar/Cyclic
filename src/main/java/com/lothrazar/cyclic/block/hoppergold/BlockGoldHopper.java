package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockGoldHopper extends BlockSimpleHopper {

  public BlockGoldHopper(Properties properties) {
    super(properties.hardnessAndResistance(1.3F));
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileGoldHopper();
  }
}
