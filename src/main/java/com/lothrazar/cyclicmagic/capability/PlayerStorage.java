package com.lothrazar.cyclicmagic.capability;

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerStorage implements IStorage<IPlayerExtendedProperties> {

  @Override
  public NBTTagCompound writeNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side) {
    return instance.getDataAsNBT();
  }

  @Override
  public void readNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side, NBTBase nbt) {
    try {
      instance.setDataFromNBT((NBTTagCompound) nbt);
    }
    catch (Exception e) {
      ModCyclic.logger.error("Invalid NBT compound: " + e.getMessage());
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
  }
}