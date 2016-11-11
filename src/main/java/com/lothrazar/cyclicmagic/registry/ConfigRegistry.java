package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;

public class ConfigRegistry {
  public static ArrayList<IHasConfig> configHandlers;

  public static void init() {
    configHandlers = new ArrayList<IHasConfig>();
    
  } 
  public static void register(IHasConfig c){
    configHandlers.add(c);
  }
}
