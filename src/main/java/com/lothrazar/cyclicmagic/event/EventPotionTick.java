package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.potion.PotionBase;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPotionTick {
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    for (PotionBase effect : PotionEffectRegistry.potionEffects) {
      if (effect != null && entity.isPotionActive(effect)) {
        effect.tick(entity);
      }
    }
  }
}
