package com.lothrazar.cyclic.block.cratemini;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;


public class TileCrateMini extends TileBlockEntityCyclic implements MenuProvider {

  ItemStackHandler inventory = new ItemStackHandler(3 * 5);

  public TileCrateMini(BlockPos pos, BlockState state) {
    super(TileRegistry.CRATE_MINI.get(), pos, state);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.CRATE_MINI.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCrateMini(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag);
    tag.put(NBTINV, inventory.serializeNBT(registries));
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
