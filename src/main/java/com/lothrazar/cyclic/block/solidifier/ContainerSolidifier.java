package com.lothrazar.cyclic.block.solidifier;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSolidifier extends ContainerBase {

  TileSolidifier tile;

  public ContainerSolidifier(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.solidifier, windowId);
    tile = (TileSolidifier) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler h = tile.inputSlots;
    addSlot(new SlotItemHandler(h, 0, 37, 17));
    addSlot(new SlotItemHandler(h, 1, 37, 17 + Const.SQ));
    addSlot(new SlotItemHandler(h, 2, 37, 17 + 2 * Const.SQ));
    addSlot(new SlotItemHandler(tile.outputSlot, 0, 121, 31));
    this.endInv = h.getSlots() + 1; //for shiftclick out of the out slot
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileSolidifier.Fields.values().length);
    trackEnergy(tile);
  }

  public int getEnergy() {
    return tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.solidifier);
  }
}
