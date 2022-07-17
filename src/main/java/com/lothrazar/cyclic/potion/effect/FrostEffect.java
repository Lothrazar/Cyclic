package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class FrostEffect extends TickableEffect {

  public FrostEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess  
    LivingEntity living = event.getEntityLiving();
    int amp = living.getEffect(this).getAmplifier();
    FrostWalkerEnchantment.onEntityMoved(living, living.level, living.blockPosition(), amp);
  }
}
