package com.lothrazar.cyclic.setup;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.block.itemcollect.ScreenCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

  @Override
  public void init() {
    ScreenManager.registerFactory(CyclicRegistry.collectortileContainer, ScreenCollector::new);
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
