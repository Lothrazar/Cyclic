package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.compat.curios.CuriosRegistry;
import com.lothrazar.cyclic.event.BiomeEvents;
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
    PotionRegistry.setup(event);
    PacketRegistry.setup();
    WorldGenRegistry.setup();
    MinecraftForge.EVENT_BUS.register(new PotionEvents());
    MinecraftForge.EVENT_BUS.register(new ItemEvents());
    MinecraftForge.EVENT_BUS.register(new BiomeEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerDataEvents());
    MinecraftForge.EVENT_BUS.register(new PlayerAbilityEvents());
  }
}
