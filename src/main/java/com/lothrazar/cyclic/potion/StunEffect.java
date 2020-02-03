package com.lothrazar.cyclic.potion;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class StunEffect extends TickableEffect {

  public StunEffect(EffectType typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
    super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
  }

  @Override
  public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
    super.performEffect(entityLivingBaseIn, amplifier);
  }

  @Override
  public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    //    ModCyclic.LOGGER.info("    ___ " + this.getAttributeModifierMap().size());
    super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
  }
}
