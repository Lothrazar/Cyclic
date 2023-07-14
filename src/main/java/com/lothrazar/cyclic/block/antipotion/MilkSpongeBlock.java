package com.lothrazar.cyclic.block.antipotion;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

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
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.RADIUS.get(), 3));
    ModCyclic.LOGGER.info("SPONGE try absorb potions on " + all.size());
    for (LivingEntity e : all) {
      if (!e.getActiveEffects().isEmpty()) {
        e.removeAllEffects();
        SoundUtil.playSound(e, SoundEvents.GENERIC_DRINK);
        ModCyclic.LOGGER.info("try absorb potions on " + e);
      }
    }
  }
}
