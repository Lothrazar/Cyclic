package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.fluid.GenericFluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class SculkFluidBlock extends GenericFluidBlock {

  public SculkFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
    if (entity instanceof Player player && entity.tickCount % Const.TICKS_PER_SEC == 0) {
      if (level.isClientSide()) {
        if (level.getRandom().nextDouble() < 0.10 && ClientConfigCyclic.SCULK_FLUID_XP_SOUND.get()) {
          level.playLocalSound(pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.4F, 0.8F + level.getRandom().nextFloat() * 0.4F, false);
        }
      } else {
        if (player.totalExperience > 0) {
          int drain = state.getFluidState().isSource() ? 2 : 1;
          player.giveExperiencePoints(-drain);
        }
      }
    }
    super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
  }
}
