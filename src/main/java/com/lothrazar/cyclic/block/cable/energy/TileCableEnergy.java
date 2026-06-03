package com.lothrazar.cyclic.block.cable.energy;

import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.HolderLookup;

public class TileCableEnergy extends TileCableBase {

  final EnergyStorageWrapper energy;
  public static ModConfigSpec.IntValue BUFFERSIZE;
  public static ModConfigSpec.IntValue TRANSFER_RATE;
  //  
  // //  private final ConcurrentHashMap<Direction, LazyOptional<IEnergyStorage>> flow = new ConcurrentHashMap<>();
  private final Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();
  private int energyLastSynced = -1; //fluid tanks have 'onchanged', energy caps do not

  public TileCableEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.ENERGY_PIPE.get(), pos, state);
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, 0);
    }
    energy = new EnergyStorageWrapper(BUFFERSIZE.get(), TRANSFER_RATE.get());
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
//    final BlockEntity tile = level.getBlockEntity(posTarget);
//    if (tile == null) {
//      return;
//    }
    final IEnergyStorage itemHandlerFrom = CapabilityUtil.energy(level, posTarget, extractSide.getOpposite());
//        .getCapability(ForgeCapabilities.ENERGY, extractSide.getOpposite())
//        .orElse(null);
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
    for (final Direction outgoingSide : DirectionUtil.getAllInDifferentOrder()) {
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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, tag.getInt(f.getSerializedName() + "_incenergy"));
    }
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (Direction f : Direction.values()) {
      tag.putInt(f.getSerializedName() + "_incenergy", mapIncomingEnergy.get(f));
    }
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    super.saveAdditional(tag, registries);
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

  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    if (side != null && !CableBase.isCableBlocked(this.getBlockState(), side)) {
      return energy;
    }
    return null;
  }

}
