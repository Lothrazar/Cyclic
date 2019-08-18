package com.lothrazar.cyclic.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

  @Override
  public void init() {
    //      ScreenManager.registerFactory(ModBlocks.FIRSTBLOCK_CONTAINER, FirstBlockScreen::new);
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getInstance().world;
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return Minecraft.getInstance().player;
  }
}
