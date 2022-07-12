package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerItemCollector extends ContainerBase {

  TileItemCollector tile;

  public ContainerItemCollector(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.COLLECTOR.get(), windowId);
    tile = (TileItemCollector) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.inventory.getSlots();
    final int numRows = 2;
    for (int j = 0; j < numRows; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlot(new SlotItemHandler(tile.inventory,
            k + j * 9,
            8 + k * Const.SQ,
            82 + j * Const.SQ) {

          @Override
          public void setChanged() {
            tile.setChanged();
          }
        });
      }
    }
    addSlot(new SlotItemHandler(tile.filter, 0, 152, 9));
    layoutPlayerInventorySlots(8, 153);
    this.trackAllIntFields(tile, TileItemCollector.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.COLLECTOR.get());
  }
}
