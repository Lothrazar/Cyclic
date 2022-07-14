package com.lothrazar.cyclic.block.rotator;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileRotator extends TileBlockEntityCyclic {

  public TileRotator(BlockPos pos, BlockState state) {
    super(TileRegistry.ROTATOR.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileRotator e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileRotator e) {
    e.tick();
  }

  public void tick() {
    boolean powered = this.isPowered();
    boolean lit = this.getBlockState().getValue(BlockCyclic.LIT);
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (powered && !lit) {
      Direction currentFacing = this.getCurrentFacing();
      BlockPos target = worldPosition.relative(currentFacing);
      BlockState state = level.getBlockState(target);
      //
      if (!state.isAir() &&
          state.getDestroySpeed(level, target) >= 0) {
        //        boolean succ = 
        BlockUtil.rotateBlockValidState(level, target, currentFacing.getOpposite());
      }
    }
    //now powered and lit match
    setLitProperty(powered);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
