package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSolidifier extends ContainerBase {

  TileSolidifier tile;

  public ContainerSolidifier(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.SOLIDIFIER.get(), windowId);
    tile = (TileSolidifier) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler h = tile.inputSlots;
    addSlot(new SlotItemHandler(h, 0, 37, 17) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(h, 1, 37, 17 + Const.SQ) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(h, 2, 37, 17 + 2 * Const.SQ) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(tile.outputSlots, 0, 121, 37) {

      @Override
      public boolean mayPlace(ItemStack stack) {
        return false;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = h.getSlots() + 1; //for shiftclick out of the out slot
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileSolidifier.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.SOLIDIFIER.get());
  }
}
