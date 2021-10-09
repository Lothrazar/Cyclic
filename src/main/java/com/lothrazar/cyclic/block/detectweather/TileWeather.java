package com.lothrazar.cyclic.block.detectweather;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileWeather extends TileEntityBase {

  public TileWeather(BlockPos pos, BlockState state) {
    super(TileRegistry.DETECTORWEATHER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileWeather e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileWeather e) {
    e.tick();
  }

  public void tick() {
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (level instanceof ServerLevel) {
      //      ServerWorld sw = (ServerWorld) world;
      //      //      WeatherCommand test;
      //      IServerWorldInfo t = sw.serverLevelData;
      //      t.getThunderTime();
      //      t.getRainTime();
      //      t.getDayTime();
      int newPower = 0;
      if (level.isThundering()) {
        newPower = 2;
      }
      else if (level.isRaining()) {
        newPower = 1;
      }
      int level = this.getBlockState().getValue(BlockWeather.LEVEL);
      if (level != newPower) {
        //        level.se
        this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(BlockWeather.LEVEL, newPower));
        //        world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
      }
    } //       world.getWorldInfo().
    //now powered and lit match
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
