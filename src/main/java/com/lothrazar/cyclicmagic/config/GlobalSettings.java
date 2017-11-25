package com.lothrazar.cyclicmagic.config;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraftforge.common.config.Configuration;

public class GlobalSettings implements IHasConfig {
 
//  public static boolean fuelBarHorizontal;
  @Override
  public void syncConfig(Configuration config) {
//   GlobalSettings.fuelBarHorizontal = config.getBoolean("FuelBarHorizontal", Const.ConfigCategory.global, false, "True means fuel bar is horizontal above the machine, false means it is vertical to the right side");
  }
}
