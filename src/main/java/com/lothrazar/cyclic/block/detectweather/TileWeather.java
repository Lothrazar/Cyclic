package com.lothrazar.cyclic.block.detectweather;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.server.ServerWorld;

public class TileWeather extends TileEntityBase implements ITickableTileEntity {

  public TileWeather() {
    super(TileRegistry.DETECTORWEATHER.get());
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    //if we are going from unpowered to powered, meaning state isnt set but power is
    if (world instanceof ServerWorld) {
      //      ServerWorld sw = (ServerWorld) world;
      //      //      WeatherCommand test;
      //      IServerWorldInfo t = sw.field_241103_E_;
      //      t.getThunderTime();
      //      t.getRainTime();
      //      t.getDayTime();
      int newPower = 0;
      if (world.isThundering()) {
        newPower = 2;
      }
      else if (world.isRaining()) {
        newPower = 1;
      }
      int level = this.getBlockState().get(BlockWeather.LEVEL);
      if (level != newPower) {
        world.setBlockState(pos, this.getBlockState().with(BlockWeather.LEVEL, newPower));
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
