package com.lothrazar.cyclic.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class ServerProxy implements IProxy {

  @Override
  public void setup() {}

  @Override
  public World getClientWorld() {
    throw new IllegalStateException("Client side only code on the server");
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return null;
  }

  @Override
  public void setPlayerReach(PlayerEntity player, int currentReach) {
    player.getAttribute(ForgeMod.REACH_DISTANCE.get()).setBaseValue(currentReach);
  }
}
