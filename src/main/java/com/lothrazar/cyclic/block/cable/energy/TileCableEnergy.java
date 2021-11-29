package com.lothrazar.cyclic.block.cable.energy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergy extends TileEntityBase implements ITickableTileEntity {

  private static final int MAX = 32000;
  final CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private final Map<Direction, Integer> mapIncomingEnergy = Maps.newHashMap();
  private int energyLastSynced = -1; //fluid tanks have 'onchanged', energy caps do not

  public TileCableEnergy() {
    super(TileRegistry.energy_pipeTile);
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, 0);
    }
  }

  @Override
  public void tick() {
    this.syncEnergy();
    this.tickDownIncomingPowerFaces();
    this.tickCableFlow();
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> extractFace = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = this.getBlockState().get(extractFace);
      if (connection.isExtraction()) {
        tryExtract(extractSide);
      }
    }
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    final BlockPos posTarget = this.pos.offset(extractSide);
    final TileEntity tile = world.getTileEntity(posTarget);
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
    final int energyToExtract = itemHandlerFrom.extractEnergy(capacity, true);
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
    Iterator<List<Direction>> inDifferingOrder = Iterables
        .cycle(UtilDirection.permutateDirections(Arrays.asList(Direction.values()), 0))
        .iterator();
    if (!inDifferingOrder.hasNext()) {
      return; // might not occur in energy, error only replicated in TileCableItem
    }
    List<Direction> list = inDifferingOrder.next();
    for (final Direction outgoingSide : list) {
      final EnumProperty<EnumConnectType> outgoingFace = CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide);
      final EnumConnectType connection = this.getBlockState().get(outgoingFace);
      if (connection.isExtraction() || connection.isBlocked()) {
        continue;
      }
      if (!this.isEnergyIncomingFromFace(outgoingSide)) {
        moveEnergy(outgoingSide, MAX);
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
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && !CableBase.isCableBlocked(this.getBlockState(), side)) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      mapIncomingEnergy.put(f, tag.getInt(f.getString() + "_incenergy"));
    }
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      tag.putInt(f.getString() + "_incenergy", mapIncomingEnergy.get(f));
    }
    tag.put(NBTENERGY, energy.serializeNBT());
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

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  protected void syncEnergy() {
    //skip if clientside
    if (world.isRemote || world.getGameTime() % 20 != 0) {
      return;
    }
    final int currentEnergy = energy.getEnergyStored();
    if (currentEnergy != energyLastSynced) {
      final PacketEnergySync packetEnergySync = new PacketEnergySync(this.getPos(), currentEnergy);
      PacketRegistry.sendToAllClients(world, packetEnergySync);
      energyLastSynced = currentEnergy;
    }
  }
}
