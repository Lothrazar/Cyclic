package com.lothrazar.cyclic.block.cable.energy;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.HolderLookup;

public class TileCableEnergy extends TileCableBase {

  public static ModConfigSpec.IntValue BUFFERSIZE;
  public static ModConfigSpec.IntValue TRANSFER_RATE;

  public TileCableEnergy(BlockPos pos, BlockState state) {
    super(TileRegistry.ENERGY_PIPE.get(), pos, state);
    setIsEnergy();
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableEnergy e) {
    e.tickEnergy();
  }

  //  @Override
  public void tickEnergy() {
    this.syncEnergy();
    this.tickDownIncomingPowerFaces();
    this.tickCableFlow();
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = getBlockState().getValue(property);
      if (connection.isExtraction()) {
        tryExtractEnergyFrom(extractSide);
      }
    }
  }

  private void tickCableFlow() {
    for (final Direction outgoingSide : DirectionUtil.getAllInDifferentOrder()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
      if (connection.isExtraction() || connection.isBlocked()) {
        continue;
      }
      if (this.isEnergyCable) {
        if (!this.isEnergyIncomingFromFace(outgoingSide)) {
          moveEnergy(outgoingSide, TRANSFER_RATE.get());
        }
      }
    }
  }

  public void tickDownIncomingPowerFaces() {
    for (final Direction incomingDirection : Direction.values()) {
      if (this.isEnergyCable) {
        mapIncomingEnergy.computeIfPresent(incomingDirection, (direction, amount) -> {
          if (amount > 0) {
            amount -= 1;
          }
          return amount;
        });
      }
    }
  }


}
