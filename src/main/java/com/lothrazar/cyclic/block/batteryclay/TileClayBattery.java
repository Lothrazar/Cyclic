package com.lothrazar.cyclic.block.batteryclay;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.EnumBatteryPercent;
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
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TileClayBattery extends TileBlockEntityCyclic implements MenuProvider {

  public static ModConfigSpec.IntValue MAX;
  EnergyStorageWrapper energy;

  public TileClayBattery(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY_CLAY.get(), pos, state);
    this.needsRedstone = 0;
    energy = new EnergyStorageWrapper(MAX.get(), MAX.get() / 4);
  }

  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
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

  public void tick() {
    this.syncEnergy();
    setPercentFilled();
    if (this.isPowered()) {
      moveEnergy(Direction.DOWN, MAX.get() / 4);
    }
  }

  public void setPercentFilled() {
    BlockState st = this.getBlockState();
    if (st.hasProperty(ClayBattery.PERCENT)) {
      EnumBatteryPercent previousPercent = st.getValue(ClayBattery.PERCENT);
      EnumBatteryPercent percent = calculateRoundedPercentFilled();
      if (percent != previousPercent) {
        this.level.setBlockAndUpdate(worldPosition, st.setValue(ClayBattery.PERCENT, percent));
      }
    }
  }

  public EnumBatteryPercent calculateRoundedPercentFilled() {
    int max = MAX.get();
    int percent = (int) Math.floor((this.getEnergy() * 1.0F) / max * 10.0) * 10;
    if (percent >= 100) {
      return EnumBatteryPercent.ONEHUNDRED;
    }
    else if (percent >= 90) {
      return EnumBatteryPercent.NINETY;
    }
    else if (percent >= 80) {
      return EnumBatteryPercent.EIGHTY;
    }
    else if (percent >= 60) {
      return EnumBatteryPercent.SIXTY;
    }
    else if (percent >= 40) {
      return EnumBatteryPercent.FOURTY;
    }
    else if (percent >= 20) {
      return EnumBatteryPercent.TWENTY;
    }
    return EnumBatteryPercent.ZERO;
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
