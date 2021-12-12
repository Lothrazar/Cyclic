package com.lothrazar.cyclic.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public final class UtilEnergyStorage {
  private UtilEnergyStorage() {
  }

  public static IEnergyStorage get(final World world, final BlockPos blockPos, final Direction side) {
    return getOptCap(world, blockPos, side).resolve().orElse(null);
  }

  public static LazyOptional<IEnergyStorage> getOptCap(final World world, final BlockPos blockPos, final Direction side) {
    final TileEntity tileEntity = world.getTileEntity(blockPos);
    if (tileEntity != null) {
      return tileEntity.getCapability(CapabilityEnergy.ENERGY, side);
    }
    return LazyOptional.empty();
  }

  public static int moveEnergy(final IEnergyStorage input, final IEnergyStorage output, final int amount) {
    int outputCapacity = output.getMaxEnergyStored() - output.getEnergyStored();
    if (outputCapacity == 0) {
      return 0;
    }
    else if (outputCapacity < 0) {
      //buggy mods, most likely using a long instead of an int for capacity and it has overflowed
      outputCapacity = amount;
    }

    //first simulate
    final int amountToExtract = input.extractEnergy(Math.min(amount, outputCapacity), true);
    if (amountToExtract <= 0) {
      return 0;
    }

    //now push it into output, but find out what was ACTUALLY received
    final int amountReceived = output.receiveEnergy(amountToExtract, false);
    if (amountReceived <= 0) {
      return 0;
    }

    //now actually extract that much from here
    return input.extractEnergy(amountReceived, false);
  }
}
