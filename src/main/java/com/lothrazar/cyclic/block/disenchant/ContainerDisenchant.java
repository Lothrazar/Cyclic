package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDisenchant extends ContainerBase {

  protected TileDisenchant tile;

  public ContainerDisenchant(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.disenchanter, windowId);
    tile = (TileDisenchant) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    ItemStackHandler h = tile.inputSlots;
    this.endInv = h.getSlots() + 1;
    addSlot(new SlotItemHandler(h, 0, 24, 40));
    addSlot(new SlotItemHandler(h, 1, 48, 40));
    addSlot(new DisenchantOutputSlot(tile.outputSlot, 0, 124, 28));
    addSlot(new DisenchantOutputSlot(tile.outputSlot, 1, 124, 58));
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(tile);
    this.trackAllIntFields(tile, TileDisenchant.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.disenchanter);
  }
}
