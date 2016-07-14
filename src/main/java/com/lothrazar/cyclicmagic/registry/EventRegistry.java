package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.event.*;
import com.lothrazar.cyclicmagic.event.core.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class EventRegistry {
  private ArrayList<Object> featureEvents = new ArrayList<Object>();
  public EventRegistry() {
    featureEvents.add(new EventMobDropBuffs());
    featureEvents.add(new EventEditSign());
    featureEvents.add(new EventEnderChest());
    featureEvents.add(new EventEndermanDropBlock());
    featureEvents.add(new EventExtendedInventory());
    featureEvents.add(new EventFoodDetails());
    featureEvents.add(new EventFragileTorches());
    featureEvents.add(new EventFurnaceStardew());
    featureEvents.add(new EventGuiTerrariaButtons());
    featureEvents.add(new EventHorseFood());
    featureEvents.add(new EventKeyInput());
    featureEvents.add(new EventLadderClimb());
    featureEvents.add(new EventMobDropsReduced());
    featureEvents.add(new EventMounted());
    featureEvents.add(new EventMountedPearl());
    featureEvents.add(new EventNametagDeath());
    featureEvents.add(new EventNameVillager());
    featureEvents.add(new EventOreMined());
    featureEvents.add(new EventPassthroughAction());
    featureEvents.add(new EventPotions());
    featureEvents.add(new EventSaplingBlockGrowth());
    featureEvents.add(new EventSaplingPlantDespawn());
    featureEvents.add(new EventSignSkullName());
    featureEvents.add(new EventSpawnChunks());
    featureEvents.add(new EventSpells());
    featureEvents.add(new EventPlayerData());
    featureEvents.add(new EventPlayerSleep());
    featureEvents.add(new EventAchievement());
    featureEvents.add(new EventLightningDamage());
  }
  public void syncConfig(Configuration config) {
    for (Object e : featureEvents) {
      if (e instanceof IHasConfig) {
        ((IHasConfig) e).syncConfig(config);
      }
    }
  }
  public void register() {
    for (Object e : featureEvents) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
