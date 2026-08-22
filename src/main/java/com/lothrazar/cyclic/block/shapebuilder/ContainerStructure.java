package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerStructure extends ContainerBase {

  TileStructure tile;

  public ContainerStructure(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.STRUCTURE.get(), windowId);
    tile = (TileStructure) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inventory, TileStructure.SLOT_BUILD, 61, 21) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = tile.inventory.getSlots();
    addSlot(new SlotItemHandler(tile.filter, TileStructure.SLOT_SHAPE, 8, 132) {

      @Override
      public int getMaxStackSize() {
        return 1;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(tile.filter, TileStructure.SLOT_GPS, 152, 132) {

      @Override
      public int getMaxStackSize() {
        return 1;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
//    });
    layoutPlayerInventorySlots(8, 153);
    this.trackAllIntFields(tile, TileStructure.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return CapabilityUtil.energyStored(tile.getLevel(), tile.getBlockPos());//tile.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.STRUCTURE.get());
  }
}
