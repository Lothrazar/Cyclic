package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileTerraGlass extends TileEntityBase {

  private static final int TIMER_FULL = TileTerraPreta.TIMER_FULL / 2;
  private static final int DISTANCE = TileTerraPreta.HEIGHT / 2;

  public TileTerraGlass(BlockPos pos, BlockState state) {
    super(TileRegistry.TERRAGLASS.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTerraGlass e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileTerraGlass e) {
    e.tick();
  }

  public void tick() {
    //sprinkler to ONLY whats directly above/below
    if (level.isClientSide) {
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    boolean lit = this.getBlockState().getValue(BlockBase.LIT);
    boolean newLit = canBlockSeeSky(level, worldPosition);
    if (lit != newLit) {
      this.setLitProperty(newLit);
      level.updateNeighborsAt(worldPosition, this.getBlockState().getBlock());
    }
    if (!newLit) {
      return;
    }
    for (int h = 0; h < DISTANCE; h++) {
      BlockPos current = worldPosition.below(h);
      TileTerraPreta.grow(level, current, 0.25);
    }
  }

  private boolean canBlockSeeSky(Level world, BlockPos pos) {
    if (world.canSeeSky(pos)) {
      return true;
    }
    //    world.isOutsideBuildHeight(pos)
    //    else {
    for (BlockPos blockpos1 = pos.above(); blockpos1.getY() < 256; blockpos1 = blockpos1.above()) {
      if (level.isOutsideBuildHeight(blockpos1.getY())) {
        continue;
      }
      BlockState blockstate = world.getBlockState(blockpos1);
      int opa = blockstate.getLightBlock(world, blockpos1);
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
