package com.lothrazar.cyclicmagic.block.password;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerClaimed {

  public void toggleClaimedHash(EntityPlayer player);

  public boolean isClaimedBy(EntityPlayer p);

  public boolean isClaimedBySomeone();

  public String getClaimedHash();

  public String getClaimedName();
}
