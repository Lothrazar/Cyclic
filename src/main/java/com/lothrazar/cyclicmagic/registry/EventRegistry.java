package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.event.EventConfigChanged;
import com.lothrazar.cyclicmagic.event.EventExtendedInventory;
import com.lothrazar.cyclicmagic.event.EventGuiInvoButtons;
import com.lothrazar.cyclicmagic.event.EventKeyInput;
import com.lothrazar.cyclicmagic.event.EventPlayerData;
import net.minecraftforge.common.MinecraftForge;

public class EventRegistry {
  private ArrayList<Object> events = new ArrayList<Object>();
  public void registerCoreEvents() {
    this.addEvent(new EventConfigChanged());
    this.addEvent(new EventExtendedInventory());
    this.addEvent(new EventKeyInput());
    this.addEvent(new EventPlayerData());
    this.addEvent(new EventGuiInvoButtons());
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
