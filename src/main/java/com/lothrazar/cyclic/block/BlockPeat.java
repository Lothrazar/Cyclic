package com.lothrazar.cyclic.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockPeat extends BlockBase {

  public static int FUEL_WEAK = 256;
  public static int FUEL_STRONG = 4096;
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
    List<BlockPos> waters = new ArrayList<>();
    for (BlockPos p : around) {
      //try to bake if SOURCE water is nearby
      Block bSide = world.getBlockState(p).getBlock();
      if (bSide == Blocks.WATER) {
        sidesWet++;
        waters.add(p);
      }
    }
    if (sidesWet >= 2) {
      tryBake(world, pos, waters);
    }
  }

  private void tryBake(World world, BlockPos pos, List<BlockPos> waters) {
    if (world.rand.nextDouble() < CHANCE_BAKE_PCT) {
      int drinkHere = MathHelper.nextInt(world.rand, 0, waters.size() - 1);
      world.setBlockState(waters.get(drinkHere), Blocks.AIR.getDefaultState());
      if (world.rand.nextDouble() < CHANCE_BAKE_PCT * 2) {
        //ok you won the second roll, NOW bake yourself
        world.setBlockState(pos, CyclicRegistry.peat_baked.getDefaultState());
        //      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, pos);
      }
    }
  }
}
