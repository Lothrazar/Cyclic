package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerAbilityEvents {

  private static final int DISABLE_OFFSET = 6;

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      tickFlying(player, datFile);
      tickSpec(player, datFile);
    }
  }

  private void tickSpec(PlayerEntity player, CyclicFile datFile) {
    if (datFile.spectatorTicks <= 0) {
      datFile.spectatorTicks = 0;
      return;
    }
    if (datFile.spectatorTicks > DISABLE_OFFSET) {
      player.noClip = true;
    }
    else if (datFile.spectatorTicks <= DISABLE_OFFSET) {
      player.noClip = false;
    }
    datFile.spectatorTicks--;
  }

  private void tickFlying(PlayerEntity player, CyclicFile datFile) {
    if (datFile.flyTicks <= 0) {
      datFile.flyTicks = 0;
      return;
    }
    if (datFile.flyTicks > DISABLE_OFFSET) {
      player.abilities.allowFlying = true;
      ModCyclic.LOGGER.info("allowFlying");
    }
    else if (datFile.flyTicks <= DISABLE_OFFSET) {
      player.abilities.allowFlying = false;
      player.abilities.isFlying = false;
      player.fallDistance = 0.0F;
      ModCyclic.LOGGER.info("DIsable flying");
    }
    datFile.flyTicks--;
  }
}
