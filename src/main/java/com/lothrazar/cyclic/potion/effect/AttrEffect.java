package com.lothrazar.cyclic.potion.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AttrEffect extends MobEffect {

  public AttrEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    //    PotionEffectRegistry.EFFECTS.add(this);
  }
}
