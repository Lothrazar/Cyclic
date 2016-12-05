package com.lothrazar.cyclicmagic.block.tileentity;

import net.minecraft.util.math.BlockPos;

public interface ITileSizeToggle {
  public void toggleSize();
  public void displayPreview();
  public BlockPos getTargetCenter();
}
