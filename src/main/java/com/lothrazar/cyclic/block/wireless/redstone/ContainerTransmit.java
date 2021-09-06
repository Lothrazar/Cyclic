package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTransmit extends ContainerBase {

  protected TileWirelessTransmit tile;

  public ContainerTransmit(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.wireless_transmitter, windowId);
    tile = (TileWirelessTransmit) world.getTileEntity(pos);
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
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.wireless_transmitter);
  }
}
