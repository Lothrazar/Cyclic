package com.lothrazar.cyclic.block.soil;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

public class TileTerraPreta extends TileEntityBase implements ITickableTileEntity {

  private static final int TIMER_FULL = 100;
  private static final int HEIGHT = 16;

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
      doGrowth(h);
    }
  }

  /**
   * Same as 1.12 sprinkler but target is above
   */
  @SuppressWarnings("deprecation")
  private void doGrowth(int target) {
    BlockPos current = this.getPos().up(target);
    BlockState bState = world.getBlockState(current);
    if (bState == null || bState.getBlock() == null) {
      return;
    }
    Block block = bState.getBlock();
    if (block instanceof IPlantable || block instanceof IGrowable) {
      if (block instanceof IGrowable) {
        IGrowable crop = ((IGrowable) block);
        if (crop.canGrow(world, current, bState, world.isRemote) == false
            || crop.canUseBonemeal(world, world.rand, current, bState)) {
          return;//its at full growth, stahp
        }
      }
      try {//no need to literally increase internal growth numbers, just force more  update ticks 
        //        world.scheduleBlockUpdate(current, block, world.rand.nextInt(30) + 20, 1);
        if (!world.isRemote && world instanceof ServerWorld) {
          block.randomTick(bState, (ServerWorld) world, current, world.rand);
          //          block.updateTick(world, current, bState, world.rand);
        }
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("TerraPreta by Cyclic has encountered an error while growing a plant, contact both mod authors    " + block, e);
      }
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
