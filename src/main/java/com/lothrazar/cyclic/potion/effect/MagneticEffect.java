package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class MagneticEffect extends TickableEffect {

  public MagneticEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    LivingEntity living = event.getEntityLiving();
    int amp = living.getEffect(this).getAmplifier();
    EntityUtil.moveEntityItemsInRegion(living.level, living.blockPosition(), 8 * amp, 1 + amp);
  }
}
