package com.lothrazar.cyclic.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

  void setup();

  World getClientWorld();

  PlayerEntity getClientPlayer();

  void setPlayerReach(PlayerEntity player, int currentReach);
}
