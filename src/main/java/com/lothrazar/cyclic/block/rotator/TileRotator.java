package com.lothrazar.cyclic.block.rotator;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileRotator extends TileEntityBase implements ITickableTileEntity {

  public TileRotator() {
    super(TileRegistry.ROTATOR.get());
  }

  @Override
  public void tick() {
    boolean powered = this.isPowered();
    boolean lit = this.getBlockState().get(BlockBase.LIT);
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (powered && !lit) {
      Direction currentFacing = this.getCurrentFacing();
      BlockPos target = pos.offset(currentFacing);
      BlockState state = world.getBlockState(target);
      //
      if (state.getBlock() != Blocks.AIR &&
          state.getBlockHardness(world, target) >= 0) {
        boolean succ = UtilPlaceBlocks.rotateBlockValidState(world, target, currentFacing.getOpposite());
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
