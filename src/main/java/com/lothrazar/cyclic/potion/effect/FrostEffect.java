package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class FrostEffect extends CyclicMobEffect {

  public FrostEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof LivingEntity living)) { return; }

    int amp = living.getEffect(PotionEffectRegistry.FROST_WALKER).getAmplifier();
    freezeNearbyWater(living, living.level(), living.blockPosition(), amp);
  }

  private static void freezeNearbyWater(LivingEntity entity, Level level, BlockPos pos, int amplifier) {
    if (!entity.onGround()) { return; }
    BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();
    float radius = Math.min(16, 2 + amplifier);
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
    for (BlockPos blockPos : BlockPos.betweenClosed(
        pos.offset(-(int) radius, -1, -(int) radius),
        pos.offset((int) radius, -1, (int) radius)
    )) {
      if (blockPos.closerToCenterThan(entity.position(), radius)) {
        mutablePos.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        BlockState above = level.getBlockState(mutablePos);
        if (above.isAir()) {
          BlockState below = level.getBlockState(blockPos);
          boolean isWater = below.is(Blocks.WATER) && below.getValue(LiquidBlock.LEVEL) == 0;
          if (isWater && frostedIce.canSurvive(level, blockPos)
              && level.isUnobstructed(frostedIce, blockPos, CollisionContext.empty())
              && level.mayInteract(entity instanceof Player p ? p : null, blockPos)) {
            level.setBlockAndUpdate(blockPos, frostedIce);
            level.scheduleTick(blockPos, Blocks.FROSTED_ICE, Mth.nextInt(entity.getRandom(), 60, 120));
          }
        }
      }
    }
  }
}
