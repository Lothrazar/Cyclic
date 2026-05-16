package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import com.lothrazar.cyclic.ModCyclic;

public class StunEffect extends CyclicMobEffect {

  public StunEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "stun_speed"), -50, AttributeModifier.Operation.ADD_VALUE);
  }
}
