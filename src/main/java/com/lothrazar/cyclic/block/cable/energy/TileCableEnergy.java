package com.lothrazar.cyclic.block.cable.energy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
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
    super(BlockRegistry.Tiles.energy_pipeTile);
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
      Direction outgoingSide = Direction.values()[i];
      if (this.isEnergyIncomingFromFace(outgoingSide) == false) {
        moveEnergy(outgoingSide, MAX);
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
      if (!CableBase.isCableBlocked(this.getBlockState(), side))
        return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, tag.getInt(f.getString() + "_incenergy"));
    }
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      tag.putInt(f.getString() + "_incenergy", mapIncomingEnergy.get(f));
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

  @Override
  public void setField(int field, int value) {}
}
