package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.config.GlobalSettings;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import net.minecraftforge.common.config.Configuration;

public class ConfigRegistry {
  public static ArrayList<IHasConfig> configHandlers;
  private static Configuration config;
  public static Configuration getConfig() {
    return config;
  }
  public static void init(Configuration c) {
    config = c;
    config.load();
    configHandlers = new ArrayList<IHasConfig>();
    configHandlers.add(new GlobalSettings());
  }
  public static void register(IHasConfig c) {
    configHandlers.add(c);
  }
  public static void syncAllConfig() {
    for (IHasConfig conf : ConfigRegistry.configHandlers) {
      conf.syncConfig(config);
    }
    //NOT only used by harvester machine, also scythe. so 
    //PRETTTY much a hack puting this here. but we cant put i in one item since both use it
    UtilHarvestCrops.syncConfig(config);
    config.save();
  }
}
