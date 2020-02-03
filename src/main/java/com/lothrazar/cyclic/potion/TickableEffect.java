package com.lothrazar.cyclic.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public abstract class TickableEffect extends Effect {

  protected TickableEffect(EffectType typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  public abstract void tick(LivingUpdateEvent event);
}
