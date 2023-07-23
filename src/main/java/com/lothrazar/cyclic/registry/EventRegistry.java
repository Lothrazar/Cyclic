package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.capabilities.CapabilityEvents;
import com.lothrazar.cyclic.compat.curios.CuriosRegistry;
import com.lothrazar.cyclic.event.BlockSpawnEvents;
import com.lothrazar.cyclic.event.ItemEvents;
import com.lothrazar.cyclic.event.PlayerAbilityEvents;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.event.PotionEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class EventRegistry {

  public static void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    CuriosRegistry.setup(event);
    PotionRegistry.setup();
    PacketRegistry.setup();
    MinecraftForge.EVENT_BUS.register(new PotionEvents());
    MinecraftForge.EVENT_BUS.register(new ItemEvents());
    MinecraftForge.EVENT_BUS.register(new BlockSpawnEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerDataEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerAbilityEvents());
    MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
    event.enqueueWork(() -> {
      CompostRegistry.setup();
    });
  }
}
