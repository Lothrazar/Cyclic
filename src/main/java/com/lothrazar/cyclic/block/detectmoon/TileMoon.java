package com.lothrazar.cyclic.block.detectmoon;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.block.state.BlockState;

public class TileMoon extends TileBlockEntityCyclic {

  public TileMoon(BlockPos pos, BlockState state) {
    super(TileRegistry.DETECTOR_MOON.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileMoon e) {
    e.tick();
  }

  public void tick() {
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (level instanceof ServerLevel) {
      ServerLevel sw = (ServerLevel) level;
      if (sw == null) {
        return;
      }
      try {
        //        int newPower = sw.getMoonPhase();
        // 26.1: DimensionType#moonPhase(long dayTime) removed - moon phase is now an environment
        // attribute, queried per-position (MoonPhase enum, .index() gives the same 0-7 value as before)
        MoonPhase moonPhase = sw.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, worldPosition);
        int newPower = moonPhase.index();
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
