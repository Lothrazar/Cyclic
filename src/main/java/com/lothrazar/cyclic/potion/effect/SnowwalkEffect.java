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
    LivingEntity entity = event.getEntityLiving();
    Level world = entity.getLevel();
    BlockPos blockpos = entity.blockPosition();
    BlockState blockstate = Blocks.SNOW.defaultBlockState();
    if (world.isEmptyBlock(blockpos) && blockstate.canSurvive(world, blockpos)) {
      //world.getBlockState(blockpos).is(Blocks.AIR)) {
      //is air
      world.setBlockAndUpdate(blockpos, blockstate);
    }
  }
}
