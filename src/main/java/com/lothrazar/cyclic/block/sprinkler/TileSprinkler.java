package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.terrasoil.TileTerraPreta;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

public class TileSprinkler extends TileEntityBase implements ITickableTileEntity {

  private static final int TIMER_FULL = TileTerraPreta.TIMER_FULL;
  private static final int RAD = 2;

  public TileSprinkler() {
    super(TileRegistry.SPRINKLER.get());
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
    }
    if (!newLit) {
      return;
    }
    final double d = 0.001;
    for (int h = 1; h <= RAD; h++) {
      TileTerraPreta.grow(world, pos.offset(Direction.NORTH, h), d);
      TileTerraPreta.grow(world, pos.offset(Direction.SOUTH, h), d);
      TileTerraPreta.grow(world, pos.offset(Direction.WEST, h), d);
      TileTerraPreta.grow(world, pos.offset(Direction.EAST, h), d);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
