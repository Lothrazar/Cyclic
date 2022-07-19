package com.lothrazar.cyclic.potion;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;

public class CyclicMobEffect extends MobEffect {

  public CyclicMobEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  public void tick(LivingUpdateEvent event) {}

  public void onPotionAdded(PotionAddedEvent event) {}

  public void isPotionApplicable(PotionApplicableEvent event) {}

  public void onPotionRemove(PotionRemoveEvent event) {}

  public void onPotionExpiry(PotionExpiryEvent event) {}
}
