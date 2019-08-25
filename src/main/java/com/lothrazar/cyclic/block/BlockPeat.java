package com.lothrazar.cyclic.block;

import java.util.List;
import java.util.Random;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPeat extends BlockBase {

  private static final double CHANCE_BAKE_PCT = 0.05;

  public BlockPeat(Properties properties) {
    super(properties.tickRandomly());
  }

  @Override
  @Deprecated
  public void tick(BlockState state, World world, BlockPos pos, Random random) {
    super.tick(state, world, pos, random);
    List<BlockPos> around = UtilShape.squareHorizontalHollow(pos, 1);
    int sidesWet = 0;
    for (BlockPos p : around) {
      //try to bake if SOURCE water is nearby
      Block bSide = world.getBlockState(p).getBlock();
      if (bSide == Blocks.WATER) {
        sidesWet++;
      }
    }
    if (sidesWet >= 2) {
      tryBake(world, pos);
    }
  }

  private void tryBake(World world, BlockPos pos) {
    if (world.rand.nextDouble() < CHANCE_BAKE_PCT) {
      //      world.setBlockToAir(pos);
      //myself is unbaked
      world.setBlockState(pos, CyclicRegistry.peat_baked.getDefaultState());
      //      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, pos);
    }
  }
}
