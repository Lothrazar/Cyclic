package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class StunEffect extends CyclicMobEffect {

  public StunEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "stun_speed"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (event.getEntity() instanceof LivingEntity entity) {
      Vec3 motion = entity.getDeltaMovement();
      entity.setDeltaMovement(0, motion.y(), 0);
    }
  }
}
