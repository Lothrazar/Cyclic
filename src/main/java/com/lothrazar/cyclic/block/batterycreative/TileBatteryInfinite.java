package com.lothrazar.cyclic.block.batterycreative;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class TileBatteryInfinite extends TileBlockEntityCyclic {

  static final int MAX = Integer.MAX_VALUE;

  static enum Fields {
    N, E, S, W, U, D;
  }

  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
  private Map<Direction, Boolean> poweredSides;

  public TileBatteryInfinite(BlockPos pos, BlockState state) {
    super(TileRegistry.BATTERY_INFINITE.get(), pos, state);
    this.needsRedstone = 0;
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
  public void loadAdditional(ValueInput input) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, input.getBooleanOr("flow_" + f.getName(), false));
    }
    energy.deserialize(input.childOrEmpty(NBTENERGY));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    for (Direction f : Direction.values()) {
      output.putBoolean("flow_" + f.getName(), poweredSides.get(f));
    }
    energy.serialize(output.child(NBTENERGY));
    super.saveAdditional(output);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBatteryInfinite e) {
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
