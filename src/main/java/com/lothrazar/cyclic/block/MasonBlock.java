package com.lothrazar.cyclic.block;

import net.minecraft.world.level.block.Block;

public class MasonBlock extends Block {

  public MasonBlock(Properties properties) {
    super(properties.requiresCorrectToolForDrops());
  }
}
