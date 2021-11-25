package com.lothrazar.cyclic.block;

import java.util.List;
import java.util.Random;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class LavaSpongeBlock extends BlockCyclic {

  public static IntValue RADIUS;

  public LavaSpongeBlock(Properties properties) {
    super(properties.randomTicks().strength(0.7F));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
    super.randomTick(state, world, pos, random);
    int r = RADIUS.get();
    List<BlockPos> around = UtilShape.cubeSquareBase(pos.below(r / 2), r, r);
    final int max = world.random.nextInt(10) + around.size() / 3;
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
