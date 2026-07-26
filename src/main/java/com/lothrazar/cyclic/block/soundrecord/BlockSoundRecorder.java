package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSoundRecorder extends BlockCyclic {

  public BlockSoundRecorder(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
    this.setHasGui();
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileSoundRecorder(pos, state);
  }
}
