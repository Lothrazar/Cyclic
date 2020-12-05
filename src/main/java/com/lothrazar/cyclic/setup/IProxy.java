package com.lothrazar.cyclic.setup;

import net.minecraft.entity.player.PlayerEntity;

public interface IProxy {

  void setup();

  PlayerEntity getClientPlayer();

  void setPlayerReach(PlayerEntity player, int currentReach);
}
