package com.lothrazar.cyclicmagic.energy;

import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyCapabilityItemStack implements ICapabilityProvider {

  public static final String NBTENERGY = "energy";
  private EnergyStore storage;

  public EnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.storage = new EnergyStore(capacity) {

      @Override
      public int getEnergyStored() {
        if (stack.hasTagCompound()) {
          return stack.getTagCompound().getInteger(NBTENERGY);
        }
        else {
          return 0;
        }
      }

      @Override
      public void setEnergyStored(int energy) {
        if (!stack.hasTagCompound()) {
          stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBTENERGY, energy);
      }
    };
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return this.getCapability(capability, facing) != null;
  }

  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.storage);
    }
    return null;
  }
}