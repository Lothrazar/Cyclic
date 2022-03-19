package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileClayBattery extends TileBlockEntityCyclic {

  private static final int SLOT_CHARGING_RATE = 8000;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  public static final int MAX = 32000;

  public TileClayBattery(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY_CLAY.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileClayBattery e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileClayBattery e) {
    e.tick();
  }

  public void tick() {
    this.syncEnergy();
    boolean isFlowing = this.isPowered();
    setLitProperty(isFlowing);
    if (isFlowing) {
      this.tickCableFlow();
    }
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override public void setField(int field, int value) {
  }

  @Override public int getField(int field) {
    return 0;
  }

  private List<Integer> rawList = IntStream.rangeClosed(0, 5).boxed().collect(Collectors.toList());

  private void tickCableFlow() {
    Collections.shuffle(rawList);
    for (Integer i : rawList) {
      Direction exportToSide = Direction.values()[i];
      moveEnergy(exportToSide, MAX / 4);
    }
  }


}
