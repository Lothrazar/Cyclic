package com.lothrazar.cyclicmagic.block.workbench;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCraftResult;

public class InventoryCraftResultMP extends InventoryCraftResult {

  protected TileEntityWorkbench tile;

  @Override
  public void markDirty() {
    if (tile != null) {
      tile.markDirty();
      IBlockState state = tile.getWorld().getBlockState(tile.getPos());
      tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 3);
    }
  }
}
