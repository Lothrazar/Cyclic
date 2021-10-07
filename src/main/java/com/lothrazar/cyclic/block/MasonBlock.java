package com.lothrazar.cyclic.block;

import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MasonBlock extends Block {

  public MasonBlock(Properties properties) {
    super(properties.requiresCorrectToolForDrops());
  }
}
