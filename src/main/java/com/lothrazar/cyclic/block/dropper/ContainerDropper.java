package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDropper extends ContainerBase {

  TileDropper tile;

  public ContainerDropper(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.DROPPER, windowId);
    tile = (TileDropper) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    if (tile instanceof TileDropper) {
      final TileDropper tileDropper = (TileDropper) tile;
      this.endInv = tileDropper.inventory.getSlots();
      addSlot(new SlotItemHandler(tileDropper.inventory, 0, 10, 51));
    }
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileDropper.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.dropper);
  }
}
