package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class WaterwalkEffect extends TickableEffect {

  public WaterwalkEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
    LivingEntity entity = event.getEntityLiving();
    if (entity.isInWater() || entity.getLevel().getBlockState(entity.blockPosition()).is(Blocks.WATER)) {
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
