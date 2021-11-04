package com.lothrazar.cyclic.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCapabilityItemStack implements ICapabilityProvider {

  public static final String NBTENERGY = "energy";
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private int max;
  private ItemStack stack;

  public EnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.max = capacity;
    this.stack = stack;
    energy = LazyOptional.of(this::createEnergy);
  }

  private IEnergyStorage createEnergy() {
    return new EnergyStorage(max, max) {

      @Override
      public int receiveEnergy(int maxReceive, boolean simulate) {
        int r = super.receiveEnergy(maxReceive, simulate);
        this.syncEnergy();
        return r;
      }

      @Override
      public int extractEnergy(int maxExtract, boolean simulate) {
        int r = super.extractEnergy(maxExtract, simulate);
        this.syncEnergy();
        return r;
      }

      private void syncEnergy() {
        if (!stack.hasTag()) {
          stack.setTag(new CompoundNBT());
        }
        stack.getTag().putInt(NBTENERGY, getEnergyStored());
      }
    };
  }

  @Override
  public String toString() {
    return "EnergyCapabilityItemStack [energy=" + energy + ", stack=" + stack + "]";
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
    if (CapabilityEnergy.ENERGY == capability) {
      return energy.cast();
    }
    return LazyOptional.empty();
  }
}
