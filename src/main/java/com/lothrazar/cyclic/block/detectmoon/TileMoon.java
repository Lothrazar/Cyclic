package com.lothrazar.cyclic.block.detectmoon;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.server.ServerWorld;

public class TileMoon extends TileEntityBase implements ITickableTileEntity {

  public TileMoon() {
    super(TileRegistry.DETECTORMOON.get());
  }

  @Override
  public void tick() {
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (world instanceof ServerWorld) {
      ServerWorld sw = (ServerWorld) world;
      if (sw == null) {
        return;
      }
      try {
        //        int newPower = sw.getMoonPhase();
        int newPower = sw.getDimensionType().getMoonPhase(sw.func_241851_ab());
        if (newPower != this.getBlockState().get(BlockMoon.LEVEL)) {
          world.setBlockState(pos, this.getBlockState().with(BlockMoon.LEVEL, newPower));
        }
      }
      catch (Exception e) {
        //
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
