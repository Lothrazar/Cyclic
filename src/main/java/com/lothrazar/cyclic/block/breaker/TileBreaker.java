package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.fan.BlockFan;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileBreaker extends TileEntityBase implements ITickableTileEntity {

  public TileBreaker() {
    super(CyclicRegistry.Tiles.breakerTile);
  }

  private void setAnimation(boolean lit) {
    this.world.setBlockState(pos, this.world.getBlockState(pos).with(BlockFan.IS_LIT, lit));
  }

  private Direction getCurrentFacing() {
    return this.getBlockState().get(BlockStateProperties.FACING);
  }

  @Override
  public void tick() {
    if (this.isPowered() == false) {
      setAnimation(false);
      return;
    }
    setAnimation(true);
    if (world.rand.nextDouble() < 0.3) {
      BlockPos target = pos.offset(this.getCurrentFacing());
      BlockState state = world.getBlockState(target);
      if (state.getBlockHardness(world, target) >= 0) {
        this.world.destroyBlock(target, true);
      }
      //else unbreakable
    }
  }

  @Override
  public void setField(int field, int value) {
    // TODO Auto-generated method stub
  }
}
