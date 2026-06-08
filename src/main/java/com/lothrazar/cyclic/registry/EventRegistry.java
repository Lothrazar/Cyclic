package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.event.*;
import com.lothrazar.cyclic.potion.PotionEventHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import com.lothrazar.cyclic.enchant.BeekeeperEnchant;
import com.lothrazar.cyclic.enchant.BeheadingEnchant;
import com.lothrazar.cyclic.enchant.DisarmEnchant;
import com.lothrazar.cyclic.enchant.EnderPearlEnchant;
import com.lothrazar.cyclic.enchant.ExcavationEnchant;
import com.lothrazar.cyclic.enchant.GloomCurseEnchant;
import com.lothrazar.cyclic.enchant.GrowthEnchant;
import com.lothrazar.cyclic.enchant.LastStandEnchant;
import com.lothrazar.cyclic.enchant.LifeLeechEnchant;
import com.lothrazar.cyclic.enchant.MagnetEnchant;
import com.lothrazar.cyclic.enchant.MultiJumpEnchant;
import com.lothrazar.cyclic.enchant.QuickdrawEnchant;
import com.lothrazar.cyclic.enchant.ReachEnchant;
import com.lothrazar.cyclic.enchant.SoulboundEnchant;
import com.lothrazar.cyclic.enchant.SteadyEnchant;
import com.lothrazar.cyclic.enchant.StepEnchant;
import com.lothrazar.cyclic.enchant.TravellerEnchant;
import com.lothrazar.cyclic.enchant.VenomEnchant;
import com.lothrazar.cyclic.enchant.XpEnchant;
import com.lothrazar.cyclic.item.equipment.MattockItem;

public class EventRegistry {

  @SubscribeEvent
  public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
    PacketRegistry.setup(event);
  }

  public static void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    NeoForge.EVENT_BUS.register(new PotionEventHandler());
    NeoForge.EVENT_BUS.register(new ItemEventHandler());
    NeoForge.EVENT_BUS.register(new EnchantEventHandler());
    NeoForge.EVENT_BUS.register(new WorldEventHandler());
    NeoForge.EVENT_BUS.register(new MattockItem.BreakHandler());
    // enchantment event handlers
    NeoForge.EVENT_BUS.register(new BeekeeperEnchant());
    NeoForge.EVENT_BUS.register(new BeheadingEnchant());
    NeoForge.EVENT_BUS.register(new DisarmEnchant());
    NeoForge.EVENT_BUS.register(new EnderPearlEnchant());
    NeoForge.EVENT_BUS.register(new ExcavationEnchant());
    NeoForge.EVENT_BUS.register(new GloomCurseEnchant());
    NeoForge.EVENT_BUS.register(new GrowthEnchant());
    NeoForge.EVENT_BUS.register(new LastStandEnchant());
    NeoForge.EVENT_BUS.register(new LifeLeechEnchant());
    NeoForge.EVENT_BUS.register(new MagnetEnchant());
    NeoForge.EVENT_BUS.register(new MultiJumpEnchant());
    NeoForge.EVENT_BUS.register(new QuickdrawEnchant());
    NeoForge.EVENT_BUS.register(new ReachEnchant());
    NeoForge.EVENT_BUS.register(new SoulboundEnchant());
    NeoForge.EVENT_BUS.register(new SteadyEnchant());
    NeoForge.EVENT_BUS.register(new StepEnchant());
    NeoForge.EVENT_BUS.register(new TravellerEnchant());
    NeoForge.EVENT_BUS.register(new VenomEnchant());
    NeoForge.EVENT_BUS.register(new XpEnchant());
    event.enqueueWork(() -> {
      CompostRegistry.setup();
    });
  }
}
