package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.event.*;
import net.minecraftforge.common.MinecraftForge;

public class EventRegistry {
  private ArrayList<Object> events = new ArrayList<Object>();
  public void registerCoreEvents() {
    this.addEvent(new EventAchievement());
    this.addEvent(new EventConfigChanged());
    this.addEvent(new EventExtendedInventory());
    this.addEvent(new EventKeyInput());
    this.addEvent(new EventPlayerData());
    this.addEvent(new EventGuiInvoButtons());
    this.addEvent(new EventPotionTick());
  }
  public void addEvent(Object e) {
    events.add(e);
  }
  public void registerAll() {
    for (Object e : events) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
