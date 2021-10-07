package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ContainerHarvester extends ContainerBase {

  protected TileHarvester tile;

  public ContainerHarvester(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.harvester, windowId);
    tile = (TileHarvester) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileHarvester.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.harvester);
  }
}
