package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerForester extends ContainerBase {

  TileForester tile;

  public ContainerForester(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.FORESTER.get(), windowId);
    tile = (TileForester) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
//    tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
      this.endInv = tile.inventory.getSlots();
      addSlot(new SlotItemHandler(tile.inventory, 0, 80, 17)); // 25
//    });
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileForester.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return CapabilityUtil.energyStored(tile.getLevel(),tile.getBlockPos()); //tile.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.FORESTER.get());
  }
}
