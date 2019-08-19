package com.lothrazar.cyclic.block.trash;

import com.lothrazar.cyclic.util.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;

public class BlockTrash extends BlockBase {

  public BlockTrash(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.METAL));
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTrash();
  }
}
