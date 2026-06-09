package com.lothrazar.cyclic.capabilities.item;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * ItemStackHandler that marks the owning BlockEntity as dirty whenever
 * its contents change. Using this in tile entities ensures that automation-
 * driven inventory changes (cables, ticking, hoppers) are persisted to disk
 * the same way that GUI-driven changes are.
 */
public class TileItemStackHandler extends ItemStackHandler {

  private final BlockEntity tile;

  public TileItemStackHandler(BlockEntity tile, int size) {
    super(size);
    this.tile = tile;
  }

  @Override
  protected void onContentsChanged(int slot) {
    if (tile != null) {
      tile.setChanged();
    }
  }
}
