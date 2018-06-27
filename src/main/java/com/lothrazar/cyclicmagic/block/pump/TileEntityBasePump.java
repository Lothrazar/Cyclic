package com.lothrazar.cyclicmagic.block.pump;

import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.IFacingBlacklist;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

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
}
