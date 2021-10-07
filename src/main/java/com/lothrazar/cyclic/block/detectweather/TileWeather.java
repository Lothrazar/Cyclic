package com.lothrazar.cyclic.block.detectweather;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.server.level.ServerLevel;

public class TileWeather extends TileEntityBase implements TickableBlockEntity {

  public TileWeather() {
    super(TileRegistry.DETECTORWEATHER.get());
  }

  @Override
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
        level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(BlockWeather.LEVEL, newPower));
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
