package com.lothrazar.cyclic.fluid.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class XpJuiceFluidBlock extends LiquidBlock {

  public XpJuiceFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }
}
