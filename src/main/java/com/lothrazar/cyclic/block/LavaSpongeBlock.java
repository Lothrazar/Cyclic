package com.lothrazar.cyclic.block;

import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class LavaSpongeBlock extends BlockCyclic {

  public static ModConfigSpec.IntValue RADIUS;

  public LavaSpongeBlock(Properties properties) {
    super(properties.randomTicks().strength(0.7F).sound(SoundType.GRASS));
  }

  @Override
  public void onPlace(BlockState bs, Level world, BlockPos pos, BlockState p_56814_, boolean p_56815_) {
    if (!p_56814_.is(bs.getBlock())) {
      this.tryAbsorbLava(world, pos);
    }
  }


  public void tryAbsorbLava(Level world, BlockPos pos) {
    int r = RADIUS.get();
    List<BlockPos> around = ShapeUtil.cubeSquareBase(pos.below(r / 2), r, r);
    final int max = world.getRandom().nextInt(10) + around.size() / 3;
    int current = 0;
    for (BlockPos posSide : around) {
      if (current > max) {
        break;
      }
      if (posSide.equals(pos)) {
        continue;
      }
      BlockState blockHere = world.getBlockState(posSide);
      FluidState fluidHere = blockHere.getFluidState();
      if (Fluids.LAVA == fluidHere.getType()) { // this check does source only, not flowing
        boolean success = world.setBlock(posSide, Blocks.AIR.defaultBlockState(), 3);
        if (success) {
          current++;
        }
      }
    }
  }
}
