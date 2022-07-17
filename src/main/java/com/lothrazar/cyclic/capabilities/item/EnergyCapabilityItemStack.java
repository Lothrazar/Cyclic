package com.lothrazar.cyclic.capabilities.item;

import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCapabilityItemStack implements ICapabilityProvider {

  public static final String NBTENERGY = "energy";
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  //  private ItemStack stack;
  private int max;
  private ItemStack stack;

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(max, max / 4) {

      @Override
      public int getEnergyStored() {
        if (stack.hasTag()) {
          return stack.getTag().getInt(NBTENERGY);
        }
        else {
          return super.getEnergyStored();
        }
      }

      @Override
      public void setEnergy(int energy) {
        if (!stack.hasTag()) {
          stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt(NBTENERGY, energy);
        super.setEnergy(energy);
      }
    };
  }

  public EnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.max = capacity;
    this.stack = stack;
    energy = LazyOptional.of(this::createEnergy);
  }

  @Override
  public String toString() {
    return "EnergyCapabilityItemStack [energy=" + energy + ", max=" + max + "]";
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
    if (CapabilityEnergy.ENERGY == capability) {
      return energy.cast();
    }
    return LazyOptional.empty();
  }
}
