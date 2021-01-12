package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PotionEvents {

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity == null) {
      return;
    }
    for (TickableEffect effect : PotionRegistry.PotionEffects.EFFECTS) {
      if (effect != null && entity.isPotionActive(effect)) {
        effect.tick(event);
      }
    }
  }
}
