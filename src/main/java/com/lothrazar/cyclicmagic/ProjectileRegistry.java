package com.lothrazar.cyclicmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import com.lothrazar.cyclicmagic.projectile.*;

public class ProjectileRegistry {

	private static Item registerProjItem(String projectileName){
		Item item = new Item();
		ItemRegistry.registerItem(item, projectileName);
		return item;
	}
	/*
	public static void spawnNew(int id,World world, EntityPlayer player){

		//TODO: find better way?
		//i dont see how since we cannot have static fns (factory constructors)
		// in interfaces. even if we had a base class
		//doesnt help, since we register the class, and spawn instances as needed
		if(id == EntityTorchBolt.ID){
			world.spawnEntityInWorld(new EntityTorchBolt(world,player));
		}
		//else if, else if, ...
		
		
	}
	*/
	public static void register(){

		int entityID = 777;

		final int trackingRange = 64; 
		final int updateFrequency = 1;
		final boolean sendsVelocityUpdates = true;
		
		EntityTorchBolt.item = registerProjItem(EntityTorchBolt.name);
		EntityRegistry.registerModEntity(EntityTorchBolt.class, EntityTorchBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	 
		
    	EntityFishingBolt.item = registerProjItem(EntityFishingBolt.name);
		EntityRegistry.registerModEntity(EntityFishingBolt.class,EntityFishingBolt.name,entityID++, 		ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntityLightningballBolt.item = registerProjItem(EntityLightningballBolt.name);
		EntityRegistry.registerModEntity(EntityLightningballBolt.class, EntityLightningballBolt.name,entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		EntitySnowballBolt.item = registerProjItem(EntitySnowballBolt.name);
		EntityRegistry.registerModEntity(EntitySnowballBolt.class, EntitySnowballBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        
		EntityBlazeBolt.item = registerProjItem(EntityBlazeBolt.name);
		EntityRegistry.registerModEntity(EntityBlazeBolt.class, EntityBlazeBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        
		EntityDynamite.item = registerProjItem(EntityDynamite.name);
		EntityRegistry.registerModEntity(EntityDynamite.class, EntityDynamite.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);

		
		EntityWaterBolt.item = registerProjItem(EntityWaterBolt.name);
		EntityRegistry.registerModEntity(EntityWaterBolt.class, EntityWaterBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	        
		EntityShearingBolt.item = registerProjItem(EntityShearingBolt.name);
		EntityRegistry.registerModEntity(EntityShearingBolt.class, EntityShearingBolt.name,entityID++, 			ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	   
 	}
}
