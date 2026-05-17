package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class MagneticEffect extends CyclicMobEffect {

  public MagneticEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (event.getEntity() instanceof LivingEntity livingEntity) {
      MobEffectInstance inst = livingEntity.getEffect(PotionEffectRegistry.MAGNETIC);
      if (inst == null) {
        return;
      }
      final int amp = inst.getAmplifier();
      EntityUtil.moveEntityItemsInRegion(livingEntity.level(), livingEntity.blockPosition(), 8 * amp, 1 + amp);
    }
  }
}
