package com.lothrazar.cyclic.potion;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;

public class CyclicMobEffect extends MobEffect {

  public CyclicMobEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  public void tick(LivingTickEvent event) {}

  public void onPotionAdded(MobEffectEvent.Added event) {}

  public void isPotionApplicable(MobEffectEvent.Applicable event) {}

  public void onPotionRemove(MobEffectEvent.Remove event) {}

  public void onPotionExpiry(MobEffectEvent.Expired event) {}
}
