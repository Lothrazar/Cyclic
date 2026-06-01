package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PotionEvents {

  @SubscribeEvent
  public void onPotionAdded(MobEffectEvent.Added event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionAdded(event);
    }
  }

  @SubscribeEvent
  public void isPotionApplicable(MobEffectEvent.Applicable event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.isPotionApplicable(event);
    }
    BlockRegistry.ANTI_BEACON.get().isPotionApplicable(event);
  }

  @SubscribeEvent
  public void onPotionRemove(MobEffectEvent.Remove event) {
    if (event.getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionRemove(event);
    }
  }

  @SubscribeEvent
  public void onPotionExpiry(MobEffectEvent.Expired event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionExpiry(event);
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof LivingEntity entity)) {
      return;
    }
    for (CyclicMobEffect effect : PotionEffectRegistry.EFFECTS) {
      if (effect != null && entity.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect))) {
        effect.tick(event);
      }
    }
  }
}
