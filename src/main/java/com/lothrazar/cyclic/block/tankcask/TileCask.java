package com.lothrazar.cyclic.block.tankcask;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.FluidTankBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TileCask extends TileBlockEntityCyclic {

  private Map<Direction, Boolean> poweredSides;
  public static final int CAPACITY = 8 * FluidType.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = CAPACITY / 2;
  public FluidTankBase tank = new FluidTankBase(this, CAPACITY, isFluidValid());

  @Override
  public IFluidHandler getFluidHandler(Direction side) {
    return tank;
  }

  static enum Fields {
    FLOWING, N, E, S, W, U, D;
  }

  public TileCask(BlockPos pos, BlockState state) {
    super(TileRegistry.CASK.get(), pos, state);
    this.needsRedstone = 0;
    flowing = 0;
    poweredSides = new HashMap<Direction, Boolean>();
    for (Direction f : Direction.values()) {
      poweredSides.put(f, false);
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCask e) {
    e.tick();
  }

  public Predicate<FluidStack> isFluidValid() {
    return p -> true;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, input.getBooleanOr("flow_" + f.getName(), false));
    }
    this.flowing = (input.getIntOr("flowing", 0));
    tank.deserialize(input.childOrEmpty(NBTFLUID));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    for (Direction f : Direction.values()) {
      output.putBoolean("flow_" + f.getName(), poweredSides.get(f));
    }
    output.putInt("flowing", this.flowing);
    tank.serialize(output.child(NBTFLUID));
    super.saveAdditional(output);
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
      case FLOWING:
        return flowing;
    }
    return -1;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case FLOWING:
        flowing = value;
        break;
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
  public void setFluid(FluidStack fluid) {
    tank.setFluid(fluid);
  }

  //  @Override
  public void tick() {
    //drain below but only to one of myself
    if (this.flowing > 0) {
      tickCableFlow();
    }
  }

  private void tickCableFlow() {
    for (final Direction exportToSide : DirectionUtil.getAllInDifferentOrder()) {
      if (this.poweredSides.get(exportToSide)) {
        this.moveFluids(exportToSide, worldPosition.relative(exportToSide), TRANSFER_FLUID_PER_TICK / 4, tank);
      }
    }
  }
}
