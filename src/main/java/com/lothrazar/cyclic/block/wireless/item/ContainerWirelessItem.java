package com.lothrazar.cyclic.block.wireless.item;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWirelessItem extends ContainerBase {

  protected TileWirelessItem tile;

  public ContainerWirelessItem(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.wireless_item, windowId);
    tile = (TileWirelessItem) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.inventory.getSlots();
    addSlot(new SlotItemHandler(tile.inventory, TileWirelessItem.SLOT_GPS, 80, 36) {

      @Override
      public int getSlotStackLimit() {
        return 1;
      }
    });
    addSlot(new SlotItemHandler(tile.inventory, TileWirelessItem.SLOT_ITEMS, 40, 36));
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileWirelessItem.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.WIRELESS_ITEM.get());
  }
}
