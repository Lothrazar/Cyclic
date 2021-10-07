package com.lothrazar.cyclic.block.wireless.item;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWirelessItem extends ContainerBase {

  protected TileWirelessItem tile;

  public ContainerWirelessItem(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.wireless_item, windowId);
    tile = (TileWirelessItem) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.inventory.getSlots() + 1; //+1 for output slot
    addSlot(new SlotItemHandler(tile.gpsSlots, 0, 80, 36) {

      @Override
      public int getMaxStackSize() {
        return 1;
      }
    });
    addSlot(new SlotItemHandler(tile.inventory, 0, 143, 36));
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileWirelessItem.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.WIRELESS_ITEM.get());
  }
}
