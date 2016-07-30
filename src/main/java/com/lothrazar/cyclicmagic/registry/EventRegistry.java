package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class EventRegistry {
  private ArrayList<Object> events = new ArrayList<Object>();
  public void addEvent(Object e){
    events.add(e);
  }
  public void syncConfig(Configuration config) {
    for (Object e : events) {
      if (e instanceof IHasConfig) {
        ((IHasConfig) e).syncConfig(config);
      }
    }
  }
  public void registerAll() {
    for (Object e : events) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
