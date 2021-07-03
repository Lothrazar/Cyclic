package com.lothrazar.cyclic.block.terraglass;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;

public class TileTerraGlass extends TileEntityBase implements ITickableTileEntity {

  private static final int TIMER_FULL = TileTerraPreta.TIMER_FULL / 2;
  private static final int DISTANCE = TileTerraPreta.HEIGHT / 2;

  public TileTerraGlass() {
    super(TileRegistry.TERRAGLASS.get());
  }

  @Override
  public void tick() {
    //sprinkler to ONLY whats directly above/below
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    boolean lit = this.getBlockState().get(BlockBase.LIT);
    boolean newLit = world.canBlockSeeSky(pos);
    if (lit != newLit) {
      this.setLitProperty(newLit);
      world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
    }
    if (!newLit) {
      return;
    }
    for (int h = 0; h < DISTANCE; h++) {
      BlockPos current = pos.down(h);
      if (!world.canBlockSeeSky(current.up())) {
        //        ModCyclic.LOGGER.info("sunbeam nogo " + h);
        return;
      }
      TileTerraPreta.grow(world, current, 0.25);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
