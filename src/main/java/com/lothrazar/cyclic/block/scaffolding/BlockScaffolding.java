package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.util.BlockRenderLayer;

public class BlockScaffolding extends BlockBase {

  public BlockScaffolding(Properties properties) {
    super(properties);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
