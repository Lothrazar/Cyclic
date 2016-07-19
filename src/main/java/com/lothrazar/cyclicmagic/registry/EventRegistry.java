package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.event.*;
import com.lothrazar.cyclicmagic.event.core.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class EventRegistry {
  private ArrayList<Object> events = new ArrayList<Object>();
  public EventRegistry() {
    events.add(new EventMobDropBuffs());
    events.add(new EventEditSign());
    events.add(new EventEnderChest());
    events.add(new EventEndermanDropBlock());
    events.add(new EventExtendedInventory());
    events.add(new EventFoodDetails());
    events.add(new EventFragileTorches());
    events.add(new EventFurnaceStardew());
    events.add(new EventGuiTerrariaButtons());
    events.add(new EventHorseFood());
    events.add(new EventKeyInput());
    events.add(new EventLadderClimb());
    events.add(new EventMobDropsReduced());
    events.add(new EventMounted());
    events.add(new EventMountedPearl());
    events.add(new EventNametagDeath());
    events.add(new EventNameVillager());
    events.add(new EventOreMined());
    events.add(new EventPassthroughAction());
    events.add(new EventPotions());
    events.add(new EventSaplingBlockGrowth());
    events.add(new EventSaplingPlantDespawn());
    events.add(new EventSignSkullName());
    events.add(new EventSpawnChunks());
    events.add(new EventSpells());
    events.add(new EventPlayerData());
    events.add(new EventPlayerSleep());
    events.add(new EventAchievement());
    events.add(new EventLightningDamage());
    events.add(new EventLootTableLoaded());
  }
  public void syncConfig(Configuration config) {
    for (Object e : events) {
      if (e instanceof IHasConfig) {
        ((IHasConfig) e).syncConfig(config);
      }
    }
  }
  public void register() {
    for (Object e : events) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
