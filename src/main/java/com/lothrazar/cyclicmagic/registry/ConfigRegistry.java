package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.config.GlobalSettings;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.util.UtilHarvester;
import com.lothrazar.cyclicmagic.util.UtilScythe;
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
    //TODO: static modules or something?
    UtilScythe.syncConfig(config);
    UtilHarvester.syncConfig(config);
    config.save();
  }
}
