package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.filesystem.CyclicFile;
import com.lothrazar.cyclic.item.elemental.FireballItem;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerAbilityEvents {

  private static final int DISABLE_OFFSET = 6;

  @SubscribeEvent
  public void onEntityUpdate(LivingTickEvent event) {
    if (event.getEntity() instanceof Player player) {
      FireballItem.tickHoldingFireball(player);
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
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
}
