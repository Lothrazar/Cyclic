package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class FrostEffect extends CyclicMobEffect {

  public FrostEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    if (!entity.onGround()) {
      return true;
    }

    Level level = entity.level();
    BlockPos pos = entity.blockPosition();
    BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();

    int radius = Math.min(16, 2 + amplifier);
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
      if (blockPos.closerToCenterThan(entity.position(), radius)) {
        mutablePos.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        BlockState blockStateAbove = level.getBlockState(mutablePos);

        if (blockStateAbove.isAir()) {
          BlockState currentBlockState = level.getBlockState(blockPos);

          if (currentBlockState.getBlock() == Blocks.WATER &&
              level.getFluidState(blockPos).isSource() &&
              frostedIce.canSurvive(level, blockPos) &&
              level.isUnobstructed(frostedIce, blockPos, CollisionContext.empty())) {

            level.setBlockAndUpdate(blockPos, frostedIce);

            level.scheduleTick(blockPos, Blocks.FROSTED_ICE, Mth.nextInt(entity.getRandom(), 60, 120));
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
    return true;
  }
}
