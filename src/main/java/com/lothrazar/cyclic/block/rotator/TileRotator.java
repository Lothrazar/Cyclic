package com.lothrazar.cyclic.block.rotator;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class TileRotator extends TileEntityBase implements TickableBlockEntity {

  public TileRotator() {
    super(TileRegistry.ROTATOR.get());
  }

  @Override
  public void tick() {
    boolean powered = this.isPowered();
    boolean lit = this.getBlockState().getValue(BlockBase.LIT);
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (powered && !lit) {
      Direction currentFacing = this.getCurrentFacing();
      BlockPos target = worldPosition.relative(currentFacing);
      BlockState state = level.getBlockState(target);
      //
      if (state.getBlock() != Blocks.AIR &&
          state.getDestroySpeed(level, target) >= 0) {
        //        boolean succ = 
        UtilPlaceBlocks.rotateBlockValidState(level, target, currentFacing.getOpposite());
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
