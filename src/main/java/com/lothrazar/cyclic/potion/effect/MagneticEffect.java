package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class MagneticEffect extends CyclicMobEffect {

  public MagneticEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
  }
}
