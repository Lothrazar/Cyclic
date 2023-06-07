package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStructure extends ContainerBase {

  TileStructure tile;

  public ContainerStructure(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.STRUCTURE.get(), windowId);
    tile = (TileStructure) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      addSlot(new SlotItemHandler(h, TileStructure.SLOT_BUILD, 61, 21) {

        @Override
        public void setChanged() {
          tile.setChanged();
        }
      });
      addSlot(new SlotItemHandler(h, TileStructure.SLOT_SHAPE, 8, 132) {

        @Override
        public int getMaxStackSize() {
          return 1;
        }

        @Override
        public void setChanged() {
          tile.setChanged();
        }
      });
      addSlot(new SlotItemHandler(h, TileStructure.SLOT_GPS, 152, 132) {

        @Override
        public int getMaxStackSize() {
          return 1;
        }

        @Override
        public void setChanged() {
          tile.setChanged();
        }
      });
      this.endInv = h.getSlots();
    });
    layoutPlayerInventorySlots(8, 153);
    this.trackAllIntFields(tile, TileStructure.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return tile.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.STRUCTURE.get());
  }
}
