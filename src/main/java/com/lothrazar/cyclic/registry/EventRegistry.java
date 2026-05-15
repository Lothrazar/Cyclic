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
    // enchantment event handlers
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.BeekeeperEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.BeheadingEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.DisarmEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.EnderPearlEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.ExcavationEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.GloomCurseEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.GrowthEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.LastStandEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.LifeLeechEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.MagnetEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.MultiJumpEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.QuickdrawEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.ReachEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.SteadyEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.StepEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.TravellerEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.VenomEnchant());
    NeoForge.EVENT_BUS.register(new com.lothrazar.cyclic.enchant.XpEnchant());
    event.enqueueWork(() -> {
      CompostRegistry.setup();
    });
  }
}
