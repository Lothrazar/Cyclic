package com.lothrazar.cyclic.capabilities;

import com.lothrazar.cyclic.capabilities.player.PlayerCapProvider;
import com.lothrazar.cyclic.registry.FeatureRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvents {

  @SubscribeEvent
  public void onWorldTick(TickEvent.LevelTickEvent event) {
    // Don't do anything client side
    if (event.level.isClientSide) {
      return;
    }
    if (event.phase == TickEvent.Phase.START) {
      return;
    }
    if (FeatureRegistry.PLAYER_SYNC_CAPS) {
      //ok then tick the manager and sync
      // Get the mana manager for this level
      CyclicWorldSavedData manager = CyclicWorldSavedData.get(event.level);
      manager.onWorldTick(event.level);
    }
  }
  // When a player dies or teleports from the end capabilities are cleared. Using the PlayerEvent.Clone event
  // we can detect this and copy our capability from the old player to the new one

  @SubscribeEvent
  public void onPlayerCloned(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      // We need to copyFrom the capabilities
      event.getOriginal().getCapability(PlayerCapProvider.CYCLIC_PLAYER).ifPresent(oldStore -> {
        event.getEntity().getCapability(PlayerCapProvider.CYCLIC_PLAYER).ifPresent(newStore -> {
          newStore.copyFrom(oldStore);
        });
      });
    }
  }
}
