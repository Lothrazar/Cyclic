package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockGoldHopper extends BlockSimpleHopper {

  public BlockGoldHopper(Properties properties) {
    super(properties.hardnessAndResistance(1.3F));
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    return Container.calcRedstone(worldIn.getTileEntity(pos));
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileGoldHopper();
  }
}
