package com.lothrazar.cyclicmagic.block;

import net.minecraft.util.EnumFacing;

public interface IFacingBlacklist {

  public boolean getBlacklist(final EnumFacing side);

  public void toggleBlacklist(final EnumFacing side);
}
