package com.lothrazar.cyclicmagic.block.cablepump;

import java.util.Map;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.IFacingBlacklist;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityBasePump extends TileEntityBaseMachineFluid implements IFacingBlacklist {

  private Map<EnumFacing, Boolean> mapBlacklist = Maps.newHashMap();

  public TileEntityBasePump(int invoSize) {
    super(invoSize);
    for (EnumFacing f : EnumFacing.values()) {
      mapBlacklist.put(f, false);
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      mapBlacklist.put(f, compound.getBoolean(f.getName() + "_blocked"));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setBoolean(f.getName() + "_blocked", mapBlacklist.get(f));
    }
    return compound;
  }

  @Override
  public boolean getBlacklist(final EnumFacing side) {
    return mapBlacklist.get(side);
  }

  @Override
  public void toggleBlacklist(final EnumFacing side) {
    mapBlacklist.put(side, !mapBlacklist.get(side));
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (mapBlacklist != null && facing != null && mapBlacklist.get(facing)) {
      return false;//announce that capability does not exist on this side. items and all.
    }
    return super.hasCapability(capability, facing);
  }
}
