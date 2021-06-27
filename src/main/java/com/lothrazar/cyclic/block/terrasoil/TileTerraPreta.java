package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TileTerraPreta extends TileEntityBase implements ITickableTileEntity {

  public static final int TIMER_FULL = 100;
  public static final int HEIGHT = 16;

  public TileTerraPreta() {
    super(TileRegistry.terra_preta);
  }

  @Override
  public void tick() {
    //sprinkler to ONLY whats directly above/below
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    for (int h = 0; h < HEIGHT; h++) {
      BlockPos current = this.getPos().up(h);
      grow(world, current, 0.5);
    }
  }

  @SuppressWarnings("deprecation")
  public static boolean grow(World world, BlockPos current, double d) {
    BlockState bState = world.getBlockState(current);
    if (bState == null || bState.getBlock() == null) {
      return false;
    }
    Block block = bState.getBlock();
    if (!isValidGrow(world, current, bState)) {
      return false;
    }
    if (world instanceof ServerWorld) {
      try {
        ServerWorld sw = (ServerWorld) world;
        block.randomTick(bState, sw, current, world.rand);
        if (world.rand.nextDouble() < d) {
          block.randomTick(bState, sw, current, world.rand);
        }
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }

  private static boolean isValidGrow(World world, BlockPos current, BlockState bState) {
    if (bState.getBlock() instanceof IGrowable) {
      IGrowable crop = ((IGrowable) bState.getBlock());
      if (!crop.canGrow(world, current, bState, world.isRemote) || !crop.canUseBonemeal(world, world.rand, current, bState)) {
        return false; //cant grow, or cant bonemeal. no
      }
    }
    //check tags
    return bState.isIn(BlockTags.CROPS) || bState.isIn(BlockTags.SAPLINGS);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
