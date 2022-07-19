package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class MagneticEffect extends CyclicMobEffect {

  public MagneticEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    LivingEntity living = event.getEntityLiving();
    int amp = living.getEffect(this).getAmplifier();
    EntityUtil.moveEntityItemsInRegion(living.level, living.blockPosition(), 8 * amp, 1 + amp);
  }
}
