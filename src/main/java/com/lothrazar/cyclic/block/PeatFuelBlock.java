package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class PeatFuelBlock extends BlockBase {

  public PeatFuelBlock(Properties properties) {
    super(properties.strength(0.6F));
  }
}
