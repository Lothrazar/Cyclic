package com.lothrazar.cyclic.block.cable.energy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergy extends TileBlockEntityCyclic {

  public static IntValue BUFFERSIZE;
  public static IntValue TRANSFER_RATE;
  CustomEnergyStorage energy;
  private LazyOptional<IEnergyStorage> energyCap;
  private final Map<Direction, Integer> mapIncomingEnergy = new ConcurrentHashMap<>();
  private int energyLastSynced = -1; //fluid tanks have 'onchanged', energy caps do not 

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
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = getBlockState().getValue(property);
      if (connection.isExtraction()) {
        tryExtract(extractSide);
      }
    }
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    final BlockPos posTarget = this.worldPosition.relative(extractSide);
    final BlockEntity tile = level.getBlockEntity(posTarget);
    if (tile == null) {
      return;
    }
    final IEnergyStorage itemHandlerFrom = tile
        .getCapability(CapabilityEnergy.ENERGY, extractSide.getOpposite())
        .orElse(null);
    if (itemHandlerFrom == null) {
      return;
    }
    final int capacity = energy.getMaxEnergyStored() - energy.getEnergyStored();
    if (capacity <= 0) {
      return;
    }
    //first we simulate 
    final int energyToExtract = itemHandlerFrom.extractEnergy(TRANSFER_RATE.get(), true);
    if (energyToExtract <= 0) {
      return;
    }
    final int energyReceived = energy.receiveEnergy(energyToExtract, false);
    if (energyReceived <= 0) {
      return;
    }
    final int energyExtracted = itemHandlerFrom.extractEnergy(energyReceived, false);
    //sanity check
    if (energyExtracted != energyReceived) {
      ModCyclic.LOGGER.error("Imbalance extracting energy, extracted " + energyExtracted + " received " + energyReceived);
    }
  }

  private void tickCableFlow() {
    for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
      if (connection.isExtraction() || connection.isBlocked()) {
        continue;
      }
      if (!this.isEnergyIncomingFromFace(outgoingSide)) {
        moveEnergy(outgoingSide, TRANSFER_RATE.get());
      }
    }
  }

  public void tickDownIncomingPowerFaces() {
    for (final Direction incomingDirection : Direction.values()) {
      mapIncomingEnergy.computeIfPresent(incomingDirection, (direction, amount) -> {
        if (amount > 0) {
          amount -= 1;
        }
        return amount;
      });
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

  @Override
  protected void syncEnergy() {
    //skip if clientside
    if (level.isClientSide || level.getGameTime() % 20 != 0) {
      return;
    }
    final int currentEnergy = energy.getEnergyStored();
    if (currentEnergy != energyLastSynced) {
      super.syncEnergy();
      energyLastSynced = currentEnergy;
    }
  }
}
