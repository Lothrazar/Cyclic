package com.lothrazar.cyclic.block.creativebattery;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileBatteryInfinite extends TileEntityBase implements ITickableTileEntity {

  static final int MAX = 960000000;

  static enum Fields {
    N, E, S, W, U, D;
  }

  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX) {
    @Override
    public boolean canReceive() {
      return false;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
      return 0;
    }
  };
  private Map<Direction, Boolean> poweredSides;
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  public TileBatteryInfinite() {
    super(TileRegistry.battery_infinite);
    poweredSides = new HashMap<>();
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
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      poweredSides.put(f, tag.getBoolean("flow_" + f.getName2()));
    }
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      tag.putBoolean("flow_" + f.getName2(), poweredSides.get(f));
    }
    tag.put(NBTENERGY, energy.serializeNBT());
    return super.write(tag);
  }

  //  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    energy.setEnergy(MAX);
    //now go
    this.tickCableFlow();
  }

  private void tickCableFlow() {
    for (final Direction exportToSide : UtilDirection.getAllInDifferentOrder()) {
      if (this.poweredSides.get(exportToSide)) {
        moveEnergyToAdjacent(energy, exportToSide, MAX / 4);
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
}
