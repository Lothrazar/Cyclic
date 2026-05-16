package com.lothrazar.cyclic.block.batterycreative;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TileBatteryInfinite extends TileBlockEntityCyclic {

  static final int MAX = Integer.MAX_VALUE;

  static enum Fields {
    N, E, S, W, U, D;
  }

  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  private Map<Direction, Boolean> poweredSides;

  public TileBatteryInfinite(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY_INFINITE.get(), pos, state);
    poweredSides = new HashMap<Direction, Boolean>();
    for (Direction f : Direction.values()) {
      poweredSides.put(f, true);
    }
  }

  public boolean getSideHasPower(Direction side) {
    return this.poweredSides.get(side);
  }

  public int getSideField(Direction side) {
    return this.getSideHasPower(side) ? 1 : 0;
  }

  public void setSideField(Direction side, int pow) {
    this.poweredSides.put(side, (pow == 1));
  }



  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, tag.getBoolean("flow_" + f.getName()));
    }
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      tag.putBoolean("flow_" + f.getName(), poweredSides.get(f));
    }
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBatteryInfinite e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileBatteryInfinite e) {
    e.tick();
  }

  public void tick() {
    energy.receiveEnergy(MAX, false);
    //now go
    this.tickCableFlow();
  }

  private void tickCableFlow() {
    for (final Direction exportToSide : DirectionUtil.getAllInDifferentOrder()) {
      if (this.poweredSides.get(exportToSide)) {
        moveEnergy(exportToSide, MAX / 4);
      }
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case D:
        return this.getSideField(Direction.DOWN);
      case E:
        return this.getSideField(Direction.EAST);
      case N:
        return this.getSideField(Direction.NORTH);
      case S:
        return this.getSideField(Direction.SOUTH);
      case U:
        return this.getSideField(Direction.UP);
      case W:
        return this.getSideField(Direction.WEST);
    }
    return -1;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case D:
        this.setSideField(Direction.DOWN, value % 2);
      break;
      case E:
        this.setSideField(Direction.EAST, value % 2);
      break;
      case N:
        this.setSideField(Direction.NORTH, value % 2);
      break;
      case S:
        this.setSideField(Direction.SOUTH, value % 2);
      break;
      case U:
        this.setSideField(Direction.UP, value % 2);
      break;
      case W:
        this.setSideField(Direction.WEST, value % 2);
      break;
    }
  }

  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
