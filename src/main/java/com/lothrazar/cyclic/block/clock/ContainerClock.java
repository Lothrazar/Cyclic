package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ContainerClock extends ContainerBase {

  protected TileRedstoneClock tile;

  public ContainerClock(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.CLOCK, windowId);
    tile = (TileRedstoneClock) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileRedstoneClock.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.clock);
  }
}
