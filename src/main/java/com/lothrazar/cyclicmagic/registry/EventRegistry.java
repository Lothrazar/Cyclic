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
  public void registerAll() {
    for (Object e : events) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
