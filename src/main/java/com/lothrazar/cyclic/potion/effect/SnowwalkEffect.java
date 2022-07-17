package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class SnowwalkEffect extends TickableEffect {

  public SnowwalkEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }

  @Override
  public void tick(LivingUpdateEvent event) {
    // delete me i guess 
    LivingEntity living = event.getEntityLiving();
    Level world = living.getLevel();
    BlockPos blockpos = living.blockPosition();
    BlockState blockstate = Blocks.SNOW.defaultBlockState();
    living.getEffect(this).getAmplifier(); // TODO: radius? 
    if (world.isEmptyBlock(blockpos) && blockstate.canSurvive(world, blockpos)) {
      //world.getBlockState(blockpos).is(Blocks.AIR)) {
      //is air
      world.setBlockAndUpdate(blockpos, blockstate);
    }
  }
}
