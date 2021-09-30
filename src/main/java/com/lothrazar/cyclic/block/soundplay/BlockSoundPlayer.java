package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockSoundPlayer extends BlockBase {

  public BlockSoundPlayer(Properties properties) {
    super(properties.hardnessAndResistance(1F).sound(SoundType.SCAFFOLDING));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileSoundPlayer();
  }
}
