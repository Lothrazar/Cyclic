package com.lothrazar.cyclic.block.packager;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPackager extends ContainerBase {

  TilePackager tile;

  public ContainerPackager(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.PACKAGER.get(), windowId);
    tile = (TilePackager) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inputSlots, 0, 51, 41) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(tile.outputSlots, 0, 91 + 4, 37 + 4) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = tile.inputSlots.getSlots() + 1;
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TilePackager.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.PACKAGER.get());
  }
}
