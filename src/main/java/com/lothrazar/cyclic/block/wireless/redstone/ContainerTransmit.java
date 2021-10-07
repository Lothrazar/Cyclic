package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTransmit extends ContainerBase {

  protected TileWirelessTransmit tile;

  public ContainerTransmit(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.WIRELESS_TRANSMITTER, windowId);
    tile = (TileWirelessTransmit) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      for (int s = 0; s < h.getSlots(); s++) {
        addSlot(new SlotItemHandler(h, s, 8 + 18 * s, 49));
      }
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileWirelessTransmit.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.wireless_transmitter);
  }
}
