package com.lothrazar.cyclic.block.cable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergy extends TileEntityBase implements ITickableTileEntity {

  private Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();
  private static final int MAX = 8000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

  public TileCableEnergy() {
    super(CyclicRegistry.energy_pipeTile);
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, 0);
    }
  }

  @Override
  public void tick() {
    this.tickDownIncomingPowerFaces();
    this.tickCableFlow();
  }

  private void tickCableFlow() {
    List<Integer> rawList = IntStream.rangeClosed(
        0,
        5).boxed().collect(Collectors.toList());
    Collections.shuffle(rawList);
    for (Integer i : rawList) {
      Direction exportToSide = Direction.values()[i];
      if (this.isEnergyIncomingFromFace(exportToSide) == false) {
        moveEnergy(exportToSide, MAX);
      }
    }
  }

  public void tickDownIncomingPowerFaces() {
    for (Direction f : Direction.values()) {
      if (mapIncomingEnergy.get(f) > 0)
        mapIncomingEnergy.put(f, mapIncomingEnergy.get(f) - 1);
    }
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, tag.getInt(f.getName() + "_incenergy"));
    }
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      tag.putInt(f.getName() + "_incenergy", mapIncomingEnergy.get(f));
    }
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  private static final int TIMER_SIDE_INPUT = 15;

  private boolean isEnergyIncomingFromFace(Direction face) {
    return mapIncomingEnergy.get(face) > 0;
  }

  public void updateIncomingEnergyFace(Direction inputFrom) {
    mapIncomingEnergy.put(inputFrom, TIMER_SIDE_INPUT);
  }
}
