package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class WaterwalkEffect extends CyclicMobEffect {

  public WaterwalkEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof LivingEntity entity)) return;
    //    living.getEffect(this).getAmplifier()
    if (entity.isInWater() || entity.level().getBlockState(entity.blockPosition()).is(Blocks.WATER)) {
      if (entity instanceof Player p) {
        if (p.isCrouching()) {
          return;// let them slip down into it
        }
      }
      entity.setOnGround(true); // act as if on solid ground
      entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
    }
  }
}
