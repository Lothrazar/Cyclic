package com.lothrazar.cyclic.block.cable.energy;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.net.PacketEnergySync;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergy extends TileEntityBase implements ITickableTileEntity {

  private static final int MAX = 32000;
  private static final int TIMER_SIDE_INPUT = 15;
  public final CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private final Map<Direction, EnumConnectType> connectTypeMap = new HashMap<>();
  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private final Map<Direction, LazyOptional<IEnergyStorage>> energyCapSides = new HashMap<>();
  private final Map<Direction, IEnergyStorage> energyCache = new HashMap<>();
  private final Map<Direction, TileEntityBase> adjacentTileEntityBases = new HashMap<>();
  private final Map<Direction, Integer> receivedFrom = new HashMap<>();

  public TileCableEnergy() {
    super(TileRegistry.energy_pipeTile);
  }

  @Override
  public IEnergyStorage getEnergyStorage() {
    return energy;
  }

  @Override
  protected IEnergyStorage getAdjacentEnergyStorage(final Direction side) {
    return energyCache.computeIfAbsent(side, k -> {
      adjacentTileEntityBases.remove(k);
      if (world == null) {
        return null;
      }
      final TileEntity tileEntity = world.getTileEntity(pos.offset(k));
      if (tileEntity == null) {
        return null;
      }
      else if (tileEntity instanceof TileEntityBase) {
        adjacentTileEntityBases.put(k, (TileEntityBase) tileEntity);
      }
      final LazyOptional<IEnergyStorage> optCap = tileEntity.getCapability(CapabilityEnergy.ENERGY, k.getOpposite());
      final IEnergyStorage storage = optCap.resolve().orElse(null);
      if (storage != null) {
        optCap.addListener((o) -> {
          adjacentTileEntityBases.remove(k);
          energyCache.remove(k);
          receivedFrom.remove(k);
        });
      }
      return storage;
    });
  }

  private EnumConnectType getConnectionType(final Direction side) {
    return connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
  }

  @Override
  public void setReceivedFrom(final  Direction side) {
    receivedFrom.put(side, TIMER_SIDE_INPUT);
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    final EnumConnectType oldConnectType = getConnectionType(side);
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      final LazyOptional<IEnergyStorage> sidedCap = energyCapSides.get(side);
      if (sidedCap != null) {
        sidedCap.invalidate();
        energyCapSides.remove(side);
      }
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      energyCapSides.put(side, LazyOptional.of(() -> energy));
    }
    connectTypeMap.put(side, connectType);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    for (final Iterator<Map.Entry<Direction, Integer>> it = receivedFrom.entrySet().iterator(); it.hasNext(); ) {
      final Map.Entry<Direction, Integer> entry = it.next();
      entry.setValue(entry.getValue() - 1);
      if (entry.getValue() <= 0) {
        it.remove();
      }
    }
    int remainingAmount = MAX;
    for (final Direction side : UtilDirection.getAllInDifferentOrder()) {
      final EnumConnectType connectType = getConnectionType(side);
      if (connectType == EnumConnectType.CABLE) {
        remainingAmount -= getEnergyFromAdjacent(energy, side, remainingAmount);
      }
      else if (connectType == EnumConnectType.INVENTORY && !receivedFrom.containsKey(side)) {
        final int moved = moveEnergyToAdjacent(energy, side, remainingAmount);
        if (moved <= 0) {
          continue;
        }
        remainingAmount -= moved;
        final TileEntityBase adjacentTileEntityBase = adjacentTileEntityBases.get(side);
        if (adjacentTileEntityBase != null) {
          adjacentTileEntityBase.setReceivedFrom(side.getOpposite());
        }
      }
    }

    if (world.getGameTime() % 20 != 0) {
      return;
    }

    final int currentEnergy = energy.getEnergyStored();
    if (currentEnergy != energy.energyLastSynced) {
      final PacketEnergySync packetEnergySync = new PacketEnergySync(this.getPos(), currentEnergy);
      PacketRegistry.sendToAllClients(world, packetEnergySync);
      energy.energyLastSynced = currentEnergy;
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      if (side == null) {
        return energyCap.cast();
      }
      LazyOptional<IEnergyStorage> sidedCap = energyCapSides.get(side);
      if (sidedCap == null) {
        if (getConnectionType(side) != EnumConnectType.BLOCKED) {
          sidedCap = LazyOptional.of(() -> energy);
          energyCapSides.put(side, sidedCap);
          return sidedCap.cast();
        }
      }
      else {
        return sidedCap.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    for (final LazyOptional<IEnergyStorage> sidedCap : energyCapSides.values()) {
      sidedCap.invalidate();
    }
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    energy.energyLastSynced = energy.getEnergyStored();
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
