package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class StunEffect extends TickableEffect {

  public StunEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070636", -50, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
  }
}
