package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.GrowthUtil;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileTerraGlass extends TileEntityBase implements ITickableTileEntity {

  public static IntValue TIMER_FULL;
  public static IntValue HEIGHT;

  public TileTerraGlass() {
    super(TileRegistry.TERRA_GLASS.get());
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
    timer = TIMER_FULL.get();
    boolean lit = this.getBlockState().get(BlockBase.LIT);
    boolean newLit = canBlockSeeSky(world, pos);
    if (lit != newLit) {
      this.setLitProperty(newLit);
      world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
    }
    if (!newLit) {
      return;
    }
    for (int h = 0; h < HEIGHT.get(); h++) {
      BlockPos current = pos.down(h);
      GrowthUtil.tryGrow(world, current, 0.25);
    }
  }

  private boolean canBlockSeeSky(World world, BlockPos pos) {
    if (world.canSeeSky(pos)) {
      return true;
    }
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
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
