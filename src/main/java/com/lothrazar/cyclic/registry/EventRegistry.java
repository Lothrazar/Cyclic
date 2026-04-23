package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.capabilities.CapabilityEvents;
import com.lothrazar.cyclic.event.BlockSpawnEvents;
import com.lothrazar.cyclic.event.ItemEvents;
import com.lothrazar.cyclic.event.PlayerAbilityEvents;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.event.PotionEvents;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class EventRegistry {

  @net.neoforged.bus.api.SubscribeEvent
  public static void registerNetworking(final net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
    PacketRegistry.setup(event);
  }

  public static void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    PotionRegistry.setup();
    NeoForge.EVENT_BUS.register(new PotionEvents());
    NeoForge.EVENT_BUS.register(new ItemEvents());
    NeoForge.EVENT_BUS.register(new BlockSpawnEvents());
    NeoForge.EVENT_BUS.register(new PlayerDataEvents());
    NeoForge.EVENT_BUS.register(new PlayerAbilityEvents());
    NeoForge.EVENT_BUS.register(new CapabilityEvents());
    event.enqueueWork(() -> {
      CompostRegistry.setup();
    });
  }
}
