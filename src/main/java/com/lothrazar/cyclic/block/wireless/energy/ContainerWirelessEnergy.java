package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerWirelessEnergy extends ContainerBase {

  protected TileWirelessEnergy tile;

  public ContainerWirelessEnergy(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.wireless_energy, windowId);
    tile = (TileWirelessEnergy) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.inventory.getSlots();
    addSlot(new SlotItemHandler(tile.inventory, 0, 80, 36));
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileWirelessEnergy.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.WIRELESS_ENERGY.get());
  }
}
