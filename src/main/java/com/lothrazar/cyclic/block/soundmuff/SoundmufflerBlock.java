package com.lothrazar.cyclic.block.soundmuff;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.world.level.block.SoundType;

public class SoundmufflerBlock extends BlockCyclic {

  public SoundmufflerBlock(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
  }
}
