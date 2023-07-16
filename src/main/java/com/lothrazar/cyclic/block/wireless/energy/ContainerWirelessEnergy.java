package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWirelessEnergy extends ContainerBase {

  protected TileWirelessEnergy tile;

  public ContainerWirelessEnergy(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.WIRELESS_ENERGY.get(), windowId);
    tile = (TileWirelessEnergy) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.gpsSlots.getSlots();
    for (int j = 0; j < tile.gpsSlots.getSlots(); j++) {
      int row = j / 9;
      int col = j % 9;
      int xPos = 8 + col * Const.SQ;
      int yPos = 36 + row * Const.SQ;
      addSlot(new SlotItemHandler(tile.gpsSlots, j, xPos, yPos) {

        @Override
        public void setChanged() {
          tile.setChanged();
        }

        @Override
        public int getMaxStackSize() {
          return 1;
        }
      });
    }
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileWirelessEnergy.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.WIRELESS_ENERGY.get());
  }
}
