package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.CyclicFile;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerAbilityEvents {

  private static final int DISABLE_OFFSET = 6;

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof Player) {
      Player player = (Player) event.getEntityLiving();
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      tickFlying(player, datFile);
      tickSpec(player, datFile);
    }
  }

  private void tickSpec(Player player, CyclicFile datFile) {
    if (datFile.spectatorTicks <= 0) {
      datFile.spectatorTicks = 0;
      return;
    }
    if (datFile.spectatorTicks > DISABLE_OFFSET) {
      player.noPhysics = true;
    }
    else if (datFile.spectatorTicks <= DISABLE_OFFSET) {
      player.noPhysics = false;
    }
    datFile.spectatorTicks--;
  }

  private void tickFlying(Player player, CyclicFile datFile) {
    if (datFile.flyTicks <= 0) {
      datFile.flyTicks = 0;
      return;
    }
    if (datFile.flyTicks > DISABLE_OFFSET) {
      player.abilities.mayfly = true;
      ModCyclic.LOGGER.info("allowFlying");
    }
    else if (datFile.flyTicks <= DISABLE_OFFSET) {
      player.abilities.mayfly = false;
      player.abilities.flying = false;
      player.fallDistance = 0.0F;
      ModCyclic.LOGGER.info("DIsable flying");
    }
    datFile.flyTicks--;
  }
}
