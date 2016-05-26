package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.event.EventAnimalDropBuffs;
import com.lothrazar.cyclicmagic.event.EventAppleUse;
import com.lothrazar.cyclicmagic.event.EventBucketBlocksBreak;
import com.lothrazar.cyclicmagic.event.EventConfigChanged;
import com.lothrazar.cyclicmagic.event.EventEditSign;
import com.lothrazar.cyclicmagic.event.EventEnderChest;
import com.lothrazar.cyclicmagic.event.EventEndermanDropBlock;
import com.lothrazar.cyclicmagic.event.EventSaplingPlantDespawn;
import com.lothrazar.cyclicmagic.event.EventExtendedInventory;
import com.lothrazar.cyclicmagic.event.EventFoodDetails;
import com.lothrazar.cyclicmagic.event.EventFragileTorches;
import com.lothrazar.cyclicmagic.event.EventFurnaceStardew;
import com.lothrazar.cyclicmagic.event.EventGuiTerrariaButtons;
import com.lothrazar.cyclicmagic.event.EventHorseFood;
import com.lothrazar.cyclicmagic.event.EventKeyInput;
import com.lothrazar.cyclicmagic.event.EventLadderClimb;
import com.lothrazar.cyclicmagic.event.EventMobDropsReduced;
import com.lothrazar.cyclicmagic.event.EventMounted;
import com.lothrazar.cyclicmagic.event.EventMountedPearl;
import com.lothrazar.cyclicmagic.event.EventNameVillager;
import com.lothrazar.cyclicmagic.event.EventNametagDeath;
import com.lothrazar.cyclicmagic.event.EventNoclipUpdate;
import com.lothrazar.cyclicmagic.event.EventOreMined;
import com.lothrazar.cyclicmagic.event.EventPassthroughAction;
import com.lothrazar.cyclicmagic.event.EventPlayerData;
import com.lothrazar.cyclicmagic.event.EventPlayerDeathCoords;
import com.lothrazar.cyclicmagic.event.EventPlayerSleep;
import com.lothrazar.cyclicmagic.event.EventPlayerWakeup;
import com.lothrazar.cyclicmagic.event.EventPotions;
import com.lothrazar.cyclicmagic.event.EventSaplingBlockGrowth;
import com.lothrazar.cyclicmagic.event.EventSignSkullName;
import com.lothrazar.cyclicmagic.event.EventSpawnChunks;
import com.lothrazar.cyclicmagic.event.EventSpells;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class EventRegistry {
	
	private ArrayList<IHasConfig> featureEvents = new ArrayList<IHasConfig>();
	
	public EventRegistry(){
		featureEvents.add(new EventAnimalDropBuffs());
		featureEvents.add(new EventAppleUse());
		featureEvents.add(new EventBucketBlocksBreak());
		featureEvents.add(new EventConfigChanged());
		featureEvents.add(new EventEditSign());
		featureEvents.add(new EventEnderChest());
		featureEvents.add(new EventEndermanDropBlock());
		featureEvents.add(new EventSaplingPlantDespawn());
		featureEvents.add(new EventExtendedInventory());
		featureEvents.add(new EventFoodDetails());	
		featureEvents.add(new EventFragileTorches());		
		featureEvents.add(new EventFurnaceStardew());	
		//EventGuiInventory is done in client proxy
		featureEvents.add(new EventGuiTerrariaButtons());	
		featureEvents.add(new EventHorseFood());
		featureEvents.add(new EventKeyInput());
		featureEvents.add(new EventLadderClimb());	
		featureEvents.add(new EventMobDropsReduced());
		featureEvents.add(new EventMounted());	
		featureEvents.add(new EventMountedPearl());
		featureEvents.add(new EventNametagDeath());
		featureEvents.add(new EventNameVillager());
		featureEvents.add(new EventNoclipUpdate());
		featureEvents.add(new EventOreMined());
		featureEvents.add(new EventPassthroughAction()); 
		featureEvents.add(new EventPlayerDeathCoords());
		featureEvents.add(new EventPlayerWakeup());
		featureEvents.add(new EventPotions());
		featureEvents.add(new EventSaplingBlockGrowth());
		featureEvents.add(new EventSignSkullName());
		featureEvents.add(new EventSpawnChunks());
		featureEvents.add(new EventSpells());
		featureEvents.add(new EventPlayerData());
		featureEvents.add(new EventPlayerSleep());
		
	}
 
	public void syncConfig(Configuration config) {
		for (IHasConfig e : featureEvents) {
			e.syncConfig(config);
		}
	}

	public void register() {
		for (IHasConfig e : featureEvents) {
			MinecraftForge.EVENT_BUS.register(e);
		}
	}
}
