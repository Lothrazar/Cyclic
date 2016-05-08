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
		featureEvents.add(new EventSaplingBlockGrowth());
		featureEvents.add(new EventPotions());
		featureEvents.add(new EventSpells());
		featureEvents.add(new EventKeyInput());
		featureEvents.add(new EventHorseFood());
		featureEvents.add(new EventGuiTerrariaButtons());
		featureEvents.add(new EventExtendedInventory());
		featureEvents.add(new EventOreMined());
		featureEvents.add(new EventMounted());
		featureEvents.add(new EventSpawnChunks());
		featureEvents.add(new EventPassthroughAction());
		featureEvents.add(new EventEnderChest());
		featureEvents.add(new EventEntityItemExpire());

		featureEvents.add(new EventLadderClimb());
		
		featureEvents.add(new EventFoodDetails());
		
		featureEvents.add(new EventFragileTorches());
		
		featureEvents.add(new EventFurnaceStardew());
		 
		featureEvents.add(new EventMountedPearl());
		featureEvents.add(new EventMobDropsReduced());

		featureEvents.add(new EventNametagDeath());
		featureEvents.add(new EventEditSign());
		featureEvents.add(new EventNameVillager());
		featureEvents.add(new EventEndermanDropBlock());
		featureEvents.add(new EventPlayerDeathCoords());
		featureEvents.add(new EventPlayerWakeup());
		featureEvents.add(new EventSignSkullName());
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
