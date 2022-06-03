package com.lothrazar.cyclic.block.beaconpotion;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPotion extends ContainerBase {

  TilePotion tile;

  public ContainerPotion(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.BEACON, windowId);
    tile = (TilePotion) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    //    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    this.endInv = tile.inventory.getSlots();
    addSlot(new SlotItemHandler(tile.inventory, 0, 9, 35) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv++;
    addSlot(new SlotItemHandler(tile.filter, 0, 149, 9) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TilePotion.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.BEACON.get());
  }
}
