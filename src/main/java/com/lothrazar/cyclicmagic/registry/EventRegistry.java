package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class EventRegistry {
  private ArrayList<Object> events = new ArrayList<Object>();
  public void addFeatureEvents() {
    events.add(new EventAchievement());
    events.add(new EventEditSign());
    events.add(new EventEnderChest());
    events.add(new EventEndermanDropBlock());
    events.add(new EventFoodDetails());
    events.add(new EventFragileTorches());
    events.add(new EventFurnaceStardew());
    events.add(new EventGuiTerrariaButtons());
    events.add(new EventLadderClimb());
    events.add(new EventLightningDamage());
    events.add(new EventLootTableLoaded());
    events.add(new EventMobDropsBuffs());
    events.add(new EventMobDropsReduced());
    events.add(new EventMounted());
    events.add(new EventMountedPearl());
    events.add(new EventNametagDeath());
    events.add(new EventNameVillager());
    events.add(new EventOreMined());
    events.add(new EventPassthroughAction());
    events.add(new EventPlayerSleep());
    events.add(new EventPotions());
    events.add(new EventSaplingBlockGrowth());
    events.add(new EventSaplingPlantDespawn());
    events.add(new EventSignSkullName());
    events.add(new EventSpawnChunks());
    events.add(new EventSpells());//so far only used by cyclic wand...
  }
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
