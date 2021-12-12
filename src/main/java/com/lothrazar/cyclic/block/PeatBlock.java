package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PeatBlock extends BlockBase {

  public static final int FUEL_WEAK = 256;
  public static final int FUEL_STRONG = 4096;

  public PeatBlock(Properties properties) {
    super(properties.tickRandomly().hardnessAndResistance(0.9F));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    super.randomTick(state, world, pos, random);
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
    if (sidesWet >= 0) {
      tryBake(world, pos, waters.size());
    }
  }

  /**
   * Percent chance to bake based on how much water. If touching 1 water source its just the PCT. Touching all 6 water sources means 6 * PCT
   * 
   */
  private void tryBake(World world, BlockPos pos, int waters) {
    if (world.rand.nextDouble() < ConfigRegistry.PEATCHANCE.get() * waters) {
      world.setBlockState(pos, BlockRegistry.peat_baked.getDefaultState());
    }
  }
}
