package com.lothrazar.cyclic.block.cable.energy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergy extends TileBlockEntityCyclic {

  public static IntValue BUFFERSIZE;
  public static IntValue TRANSFER_RATE;
  CustomEnergyStorage energy;
  private LazyOptional<IEnergyStorage> energyCap;
  private Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();

  public TileCableEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.ENERGY_PIPE.get(), pos, state);
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, 0);
    }
    energy = new CustomEnergyStorage(BUFFERSIZE.get(), BUFFERSIZE.get());
    energyCap = LazyOptional.of(() -> energy);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableEnergy e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileCableEnergy e) {
    e.tick();
  }

  //  @Override
  public void tick() {
    this.syncEnergy();
    this.tickDownIncomingPowerFaces();
    this.tickCableFlow();
    //extract mode conditionally
    for (Direction side : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(side));
      if (connection.isExtraction()) {
        tryExtract(side);
      }
    }
  }

  @SuppressWarnings("unused")
  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    BlockPos posTarget = this.worldPosition.relative(extractSide);
    BlockEntity tile = level.getBlockEntity(posTarget);
    if (tile != null) {
      IEnergyStorage itemHandlerFrom = tile.getCapability(ForgeCapabilities.ENERGY, extractSide.getOpposite()).orElse(null);
      if (itemHandlerFrom != null) {
        //ok go
        //
        int extractSim = itemHandlerFrom.extractEnergy(TRANSFER_RATE.get(), true);
        if (extractSim > 0 && energy.receiveEnergy(extractSim, true) > 0) {
          //actually extract energy for real, whatever it accepted 
          int actuallyEx = itemHandlerFrom.extractEnergy(energy.receiveEnergy(extractSim, false), false);
        }
      }
    }
  }

  private void tickCableFlow() {
    List<Integer> rawList = IntStream.rangeClosed(0, 5).boxed().collect(Collectors.toList());
    Collections.shuffle(rawList);
    for (Integer i : rawList) {
      Direction outgoingSide = Direction.values()[i];
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
      if (connection.isExtraction() || connection.isBlocked()) {
        continue;
      }
      if (this.isEnergyIncomingFromFace(outgoingSide) == false) {
        moveEnergy(outgoingSide, TRANSFER_RATE.get());
      }
    }
  }

  public void tickDownIncomingPowerFaces() {
    for (Direction f : Direction.values()) {
      if (mapIncomingEnergy.get(f) > 0) {
        mapIncomingEnergy.put(f, mapIncomingEnergy.get(f) - 1);
      }
    }
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return energyCap.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, tag.getInt(f.getSerializedName() + "_incenergy"));
    }
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    for (Direction f : Direction.values()) {
      tag.putInt(f.getSerializedName() + "_incenergy", mapIncomingEnergy.get(f));
    }
    tag.put(NBTENERGY, energy.serializeNBT());
    super.saveAdditional(tag);
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

  @Override
  public int getField(int field) {
    return 0;
  }
}
