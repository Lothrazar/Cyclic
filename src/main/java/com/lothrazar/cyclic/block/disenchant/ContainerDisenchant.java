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
    ItemStackHandler inputSlots = tile.inputSlots;
    ItemStackHandler outputSlots = tile.outputSlots;
    this.endInv = inputSlots.getSlots() + outputSlots.getSlots();
    addSlot(new SlotItemHandler(inputSlots, 0, 24, 40));
    addSlot(new SlotItemHandler(inputSlots, 1, 48, 40));
    addSlot(new DisenchantOutputSlot(outputSlots, 0, 108, 24));
    addSlot(new DisenchantOutputSlot(outputSlots, 1, 108, 56));
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(tile);
    this.trackAllIntFields(tile, TileDisenchant.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.disenchanter);
  }
}
