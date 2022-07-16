package com.lothrazar.cyclic.block.antipotion;

import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MilkSpongeBlock extends BlockCyclic {

  public MilkSpongeBlock(Properties properties) {
    super(properties.randomTicks().strength(0.7F));
  }

  @Override
  public void onPlace(BlockState bs, Level world, BlockPos pos, BlockState bsIn, boolean p_56815_) {
    if (!bsIn.is(bs.getBlock())) {
      this.absorbPotions(world, pos);
    }
  }

  public void absorbPotions(Level world, BlockPos pos) {
    List<LivingEntity> all = world.getEntitiesOfClass(LivingEntity.class, EntityUtil.makeBoundingBox(pos, TileAntiBeacon.RADIUS.get(), 3));
    for (LivingEntity e : all) {
      e.removeAllEffects();
    }
  }
}
