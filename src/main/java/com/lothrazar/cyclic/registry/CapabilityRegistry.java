package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.capabilities.ManaManager;
import com.lothrazar.cyclic.capabilities.player.PlayerManaProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityRegistry {
  // Whenever a new object of some type is created the AttachCapabilitiesEvent will fire. In our case we want to know
  // when a new player arrives so that we can attach our capability here

  //  @SubscribeEvent not sub, uses  MinecraftForge.EVENT_BUS.addGenericListener instead
  public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      if (!event.getObject().getCapability(PlayerManaProvider.PLAYER_MANA).isPresent()) {
        // The player does not already have this capability so we need to add the capability provider here
        event.addCapability(new ResourceLocation(ModCyclic.MODID, "data"), new PlayerManaProvider());
        System.out.println("CapabilityRegistry success for data");
      }
    }
  }

  @SubscribeEvent
  public void onWorldTick(TickEvent.WorldTickEvent event) {
    // Don't do anything client side
    if (event.world.isClientSide) {
      return;
    }
    if (event.phase == TickEvent.Phase.START) {
      return;
    }
    // Get the mana manager for this level
    ManaManager manager = ManaManager.get(event.world);
    manager.tick(event.world);
  }
  // When a player dies or teleports from the end capabilities are cleared. Using the PlayerEvent.Clone event
  // we can detect this and copy our capability from the old player to the new one

  @SubscribeEvent
  public void onPlayerCloned(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      // We need to copyFrom the capabilities
      event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldStore -> {
        event.getPlayer().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newStore -> {
          newStore.copyFrom(oldStore);
        });
      });
    }
  }
  // Finally we need to register our capability in a RegisterCapabilitiesEvent 

  @SubscribeEvent
  public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    event.register(ManaManager.PlayerCapabilityStorage.class);
    System.out.println("registry success for ManaManager");
  }
}
