package com.lothrazar.cyclic.block;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;

public class MetalBarsBlock extends IronBarsBlock {

  public MetalBarsBlock(Properties prop) {
    super(prop.sound(SoundType.METAL).noOcclusion());
  }

}
