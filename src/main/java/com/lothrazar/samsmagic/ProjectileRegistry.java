package com.lothrazar.samsmagic;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import com.lothrazar.samsmagic.projectile.*;

public class ProjectileRegistry {

	public static void register(){

		int entityID = 777;

		final int trackingRange = 64; 
		final int updateFrequency = 1;
		final boolean sendsVelocityUpdates = true;

    	  
    	EntityFishingBolt.item = new Item();
		ItemRegistry.registerItem(EntityFishingBolt.item, EntityFishingBolt.name_item);
		EntityRegistry.registerModEntity(EntityFishingBolt.class,EntityFishingBolt.name,entityID, 		ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntityLightningballBolt.item = new Item();
		ItemRegistry.registerItem(EntityLightningballBolt.item, EntityLightningballBolt.name_item);
		EntityRegistry.registerModEntity(EntityLightningballBolt.class, EntityLightningballBolt.name,entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntitySnowballBolt.item = new Item();
		ItemRegistry.registerItem(EntitySnowballBolt.item, EntitySnowballBolt.name_item);
		EntityRegistry.registerModEntity(EntitySnowballBolt.class, EntitySnowballBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        
		EntityBlazeBolt.item = new Item();
		ItemRegistry.registerItem(EntityBlazeBolt.item, EntityBlazeBolt.name_item);
		EntityRegistry.registerModEntity(EntityBlazeBolt.class, EntityBlazeBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        
		EntityDynamite.item = new Item();
		ItemRegistry.registerItem(EntityDynamite.item, EntityDynamite.name_item);
		EntityRegistry.registerModEntity(EntityDynamite.class, EntityDynamite.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntityTorchBolt.item = new Item();
		ItemRegistry.registerItem(EntityTorchBolt.item, EntityTorchBolt.name_item);
		EntityRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt",entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntityWaterBolt.item = new Item();
		ItemRegistry.registerItem(EntityWaterBolt.item, EntityWaterBolt.name_item);
		EntityRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt",entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	        
		EntityShearingBolt.item = new Item();
		ItemRegistry.registerItem(EntityShearingBolt.item, EntityShearingBolt.name_item);
		EntityRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt",entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	       
    	/*
        EntityRegistry.registerModEntity(EntityHarvestBolt.class, "harvestbolt",entityID++, 		ModScepterPowers.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
       */
        
 	}
}
