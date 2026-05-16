package com.lothrazar.cyclic.block.batteryclay;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TileClayBattery extends TileBlockEntityCyclic implements MenuProvider {

  public static ModConfigSpec.IntValue MAX;
  EnergyStorageWrapper energy;

  public TileClayBattery(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY_CLAY.get(), pos, state);
    energy = new EnergyStorageWrapper(MAX.get(), MAX.get() / 4);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.BATTERY_CLAY.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerClayBattery(i, level, worldPosition, playerInventory, playerEntity);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileClayBattery e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileClayBattery e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    if (this.isPowered()) {
      moveEnergy(Direction.DOWN, MAX.get() / 4);
    }
  }


  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
