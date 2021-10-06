package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileTerraGlass extends TileEntityBase implements ITickableTileEntity {

  private static final int TIMER_FULL = TileTerraPreta.TIMER_FULL / 2;
  private static final int DISTANCE = TileTerraPreta.HEIGHT / 2;

  public TileTerraGlass() {
    super(TileRegistry.TERRAGLASS.get());
  }

  @Override
  public void tick() {
    //sprinkler to ONLY whats directly above/below
    if (world.isRemote) {
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    boolean lit = this.getBlockState().get(BlockBase.LIT);
    boolean newLit = canBlockSeeSky(world, pos);
    if (lit != newLit) {
      this.setLitProperty(newLit);
      world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
    }
    if (!newLit) {
      return;
    }
    for (int h = 0; h < DISTANCE; h++) {
      BlockPos current = pos.down(h);
      TileTerraPreta.grow(world, current, 0.25);
    }
  }

  private boolean canBlockSeeSky(World world, BlockPos pos) {
    if (world.canSeeSky(pos)) {
      return true;
    }
    //    world.isOutsideBuildHeight(pos)
    //    else {
    for (BlockPos blockpos1 = pos.up(); blockpos1.getY() < 256; blockpos1 = blockpos1.up()) {
      if (World.isYOutOfBounds(blockpos1.getY())) {
        continue;
      }
      BlockState blockstate = world.getBlockState(blockpos1);
      int opa = blockstate.getOpacity(world, blockpos1);
      if (opa > 0 && !blockstate.getMaterial().isLiquid()) {
        return false;
      }
    }
    return true;
    //    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
