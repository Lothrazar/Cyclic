package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class SwimEffect extends TickableEffect {

  private static final float speedfactor = 0.09F;

  public SwimEffect(EffectType typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
    LivingEntity entity = event.getEntityLiving();
    if (entity.isInWater()) {
      EffectInstance pot = entity.getActivePotionEffect(this);
      int amp = pot.getAmplifier() + 4;//level I is zero,  II is one
      ModCyclic.LOGGER.info("Speed up by this much " + pot.getAmplifier());
      UtilEntity.speedupEntityIfMoving(entity, speedfactor * amp);
    }
  }
  //
  //  private boolean isInBoat(LivingEntity entity) {
  //    return entity.getLowestRidingEntity() instanceof BoatEntity;
  //  }
}
