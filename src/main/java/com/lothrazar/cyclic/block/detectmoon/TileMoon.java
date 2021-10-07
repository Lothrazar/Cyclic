package com.lothrazar.cyclic.block.detectmoon;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.server.level.ServerLevel;

public class TileMoon extends TileEntityBase implements TickableBlockEntity {

  public TileMoon() {
    super(TileRegistry.DETECTORMOON.get());
  }

  @Override
  public void tick() {
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (level instanceof ServerLevel) {
      ServerLevel sw = (ServerLevel) level;
      if (sw == null) {
        return;
      }
      try {
        //        int newPower = sw.getMoonPhase();
        int newPower = sw.dimensionType().moonPhase(sw.dayTime());
        if (newPower != this.getBlockState().getValue(BlockMoon.LEVEL)) {
          level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(BlockMoon.LEVEL, newPower));
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
