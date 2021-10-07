package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class SwimEffect extends TickableEffect {

  private static final float speedfactor = 0.09F;

  public SwimEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
    LivingEntity entity = event.getEntityLiving();
    if (entity.isInWater()) {
      MobEffectInstance pot = entity.getEffect(this);
      int amp = pot.getAmplifier() + 4; //level I is zero,  II is one 
      UtilEntity.speedupEntityIfMoving(entity, speedfactor * amp);
    }
  }
  //
  //  private boolean isInBoat(LivingEntity entity) {
  //    return entity.getLowestRidingEntity() instanceof BoatEntity;
  //  }
}
