package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.lothrazar.cyclicmagic.event.*;

public class EventRegistry {
	
	private ArrayList<IFeatureEvent> featureEvents = new ArrayList<IFeatureEvent>();
	
	public EventRegistry(){
		featureEvents.add(new EventAnimalDropBuffs());
		featureEvents.add(new EventAppleUse());
		featureEvents.add(new EventBucketBlocksBreak());
		featureEvents.add(new EventConfigChanged());
		featureEvents.add(new EventEditSign());
		featureEvents.add(new EventEnderChest());
		featureEvents.add(new EventEndermanDropBlock());
		featureEvents.add(new EventEntityItemExpire());
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
		featureEvents.add(new EventOreMined());
		featureEvents.add(new EventPassthroughAction()); 
		featureEvents.add(new EventPlayerDeathCoords());
		featureEvents.add(new EventPlayerWakeup());
		featureEvents.add(new EventPotions());
		featureEvents.add(new EventSaplingBlockGrowth());
		featureEvents.add(new EventSignSkullName());
		featureEvents.add(new EventSpawnChunks());
		featureEvents.add(new EventSpells());
	}
 
	public void syncConfig(Configuration config) {
		for (IFeatureEvent e : featureEvents) {
			e.syncConfig(config);
		}
	}

	public void register() {
		for (IFeatureEvent e : featureEvents) {
			MinecraftForge.EVENT_BUS.register(e);
		}
	}
}
