package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.capabilities.player.PlayerCapProvider;
import com.lothrazar.cyclic.capabilities.player.PlayerCapabilityStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityRegistry {
  // Whenever a new object of some type is created the AttachCapabilitiesEvent will fire. In our case we want to know
  // when a new player arrives so that we can attach our capability here

  //  @SubscribeEvent not sub, uses  MinecraftForge.EVENT_BUS.addGenericListener instead
  public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      if (!event.getObject().getCapability(PlayerCapProvider.CYCLIC_PLAYER).isPresent()) {
        // The player does not already have this capability so we need to add the capability provider here
        event.addCapability(new ResourceLocation(ModCyclic.MODID, "data"), new PlayerCapProvider());
      }
    }
  }

  @SubscribeEvent
  public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(PlayerCapabilityStorage.class);
  }
  // Finally we need to register our capability in a RegisterCapabilitiesEvent 
}
