package com.lothrazar.cyclic.potion;

import com.lothrazar.cyclic.CyclicRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventPotionTick {

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity == null) {
      return;
    }
    for (TickableEffect effect : CyclicRegistry.PotionEffects.effects) {
      if (effect != null && entity.isPotionActive(effect)) {
        effect.tick(event);
      }
    }
  }
}
