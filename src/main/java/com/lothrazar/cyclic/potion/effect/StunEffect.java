package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class StunEffect extends TickableEffect {

  public StunEffect(EffectType typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    this.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070636", -50, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
  }
}
