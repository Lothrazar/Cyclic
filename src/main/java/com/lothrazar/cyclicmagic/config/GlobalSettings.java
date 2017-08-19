package com.lothrazar.cyclicmagic.config;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraftforge.common.config.Configuration;

public class GlobalSettings implements IHasConfig {
  /**
   * set by config file, ability to disable fuel requirements of all machiens
   * defaults true if false, it will hide fuel progress bars and fuel input
   * slots
   */
  public static boolean fuelEnabled;
  @Override
  public void syncConfig(Configuration config) {
    GlobalSettings.fuelEnabled = config.getBoolean("MachinesNeedFuel", Const.ConfigCategory.global, true, "False will mean all machines will run for free (as they did in old versions), true means burnable fuel is required such as coal");
  }
}
