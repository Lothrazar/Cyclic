package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBattery extends ContainerBase {

  TileBattery tile;

  public ContainerBattery(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.BATTERY.get(), windowId);
    tile = (TileBattery) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.batterySlots, 0, 134, 54) {

      @Override
      public int getMaxStackSize() {
        return 1;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = 1;
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(tile);
    this.trackAllIntFields(tile, TileBattery.Fields.values().length);
  }

  public int getEnergy() {
    return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.BATTERY.get());
  }
}
