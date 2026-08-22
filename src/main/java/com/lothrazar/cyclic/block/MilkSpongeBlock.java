package com.lothrazar.cyclic.block;

import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MilkSpongeBlock extends BlockCyclic {

  public MilkSpongeBlock(Properties properties) {
    super(properties.randomTicks().strength(0.7F).sound(SoundType.GRASS));
  }

  @Override
  public void onPlace(BlockState bs, Level world, BlockPos pos, BlockState bsIn, boolean p_56815_) {
    if (!bsIn.is(bs.getBlock())) {
      this.absorbPotions(world, pos);
    }
  }

  private void absorbPotions(Level world, BlockPos pos) {
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, 3, 3));

    for (LivingEntity e : all) {
      if (!e.getActiveEffects().isEmpty()) {
        e.removeAllEffects();
        SoundUtil.playSound(e, SoundEvents.GENERIC_DRINK.value());
      }
    }
  }
}
