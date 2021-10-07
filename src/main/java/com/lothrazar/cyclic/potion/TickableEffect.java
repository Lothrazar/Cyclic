package com.lothrazar.cyclic.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public abstract class TickableEffect extends MobEffect {

  protected TickableEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  public abstract void tick(LivingUpdateEvent event);
}
