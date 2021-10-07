package com.lothrazar.cyclic.block.disenchant;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDisenchant extends ContainerBase {

  protected TileDisenchant tile;

  public ContainerDisenchant(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.DISENCHANTER, windowId);
    tile = (TileDisenchant) world.getBlockEntity(pos);
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
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.disenchanter);
  }
}
