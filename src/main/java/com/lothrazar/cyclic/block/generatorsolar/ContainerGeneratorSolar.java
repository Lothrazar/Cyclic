package com.lothrazar.cyclic.block.generatorsolar;

import com.lothrazar.cyclic.fixers.CapabilityUtil;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

public class ContainerGeneratorSolar extends ContainerBase {

  TileGeneratorSolar tile;

  public ContainerGeneratorSolar(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.GENERATOR_SOLAR.get(), windowId);
    tile = (TileGeneratorSolar) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileGeneratorSolar.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return CapabilityUtil.energyStored(tile.getLevel(),tile.getBlockPos());
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.GENERATOR_SOLAR.get());
  }
}
