package com.lothrazar.cyclicmagic.capability;

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerCapInstance implements ICapabilitySerializable<NBTTagCompound> {

  IPlayerExtendedProperties inst = ModCyclic.CAPABILITYSTORAGE.getDefaultInstance();

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == ModCyclic.CAPABILITYSTORAGE;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    return capability == ModCyclic.CAPABILITYSTORAGE ? ModCyclic.CAPABILITYSTORAGE.<T> cast(inst) : null;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTBase ret = ModCyclic.CAPABILITYSTORAGE.getStorage().writeNBT(ModCyclic.CAPABILITYSTORAGE, inst, null);
    if (ret instanceof NBTTagCompound) {
      return (NBTTagCompound) ret;
    }
    return null;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    ModCyclic.CAPABILITYSTORAGE.getStorage().readNBT(ModCyclic.CAPABILITYSTORAGE, inst, null, nbt);
  }
}