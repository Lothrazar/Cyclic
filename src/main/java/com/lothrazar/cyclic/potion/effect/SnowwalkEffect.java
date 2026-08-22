package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class SnowwalkEffect extends CyclicMobEffect {

  public SnowwalkEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof LivingEntity living)) {
      return;
    }
    Level level = living.level();
    BlockPos center = living.blockPosition();
    BlockState snow = Blocks.SNOW.defaultBlockState();
    int radius = living.getEffect(PotionEffectRegistry.SNOWWALK).getAmplifier();
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        BlockPos pos = center.offset(dx, 0, dz);
        if (level.isEmptyBlock(pos) && snow.canSurvive(level, pos)) {
          level.setBlockAndUpdate(pos, snow);
        }
      }
    }
  }
}
