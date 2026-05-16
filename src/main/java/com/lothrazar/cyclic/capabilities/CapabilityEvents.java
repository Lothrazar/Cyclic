package com.lothrazar.cyclic.capabilities;

import com.lothrazar.cyclic.registry.FeatureRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class CapabilityEvents {

  @SubscribeEvent
  public void onWorldTick(LevelTickEvent.Post event) {
    // Don't do anything client side
    if (event.getLevel().isClientSide) {
      return;
    }
    if (FeatureRegistry.PLAYER_SYNC_CAPS) {
      CyclicWorldSavedData manager = CyclicWorldSavedData.get(event.getLevel());
      manager.onWorldTick(event.getLevel());
    }
  }
}
