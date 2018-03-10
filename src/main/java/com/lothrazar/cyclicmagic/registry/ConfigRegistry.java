/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
  public static Configuration oreConfig;
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
