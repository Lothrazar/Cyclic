package com.lothrazar.cyclic.capabilities.item;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityProviderEnergyStack implements ICapabilitySerializable<CompoundTag> {

  private final CustomEnergyStorage energy;
  private final LazyOptional<IEnergyStorage> energyCap;

  public CapabilityProviderEnergyStack(int max) {
    energy = new CustomEnergyStorage(max, max);
    energyCap = LazyOptional.of(() -> energy);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.put(TileBlockEntityCyclic.NBTENERGY, energy.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    energy.deserializeNBT(nbt.getCompound(TileBlockEntityCyclic.NBTENERGY));
  }

  @Override
  public String toString() {
    return "CapabilityProviderEnergyStack [energy=" + energy + "]";
  }
}
