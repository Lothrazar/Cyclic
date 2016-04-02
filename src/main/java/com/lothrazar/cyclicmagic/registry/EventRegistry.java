package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import com.lothrazar.cyclicmagic.event.*;

public class EventRegistry{
	
	private static ArrayList<Object> events = new ArrayList<Object>();

	public static void register(){
		events.add(new EventConfigChanged());
		events.add(new EventEndermanDropBlock());
		events.add(new EventMobDrops());
		events.add(new EventNametagDeath());
		events.add(new EventNameVillager());
		events.add(new EventPlayerDeath());
		events.add(new EventPlayerWakeup());
		events.add(new EventPotions());
		events.add(new EventSignSkullName());
		events.add(new EventSpells());
		
		for(Object e : events){

			MinecraftForge.EVENT_BUS.register(e);
		}
	}


/*
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{ 
		
		if(event.entity instanceof EntityLivingBase && event.world.isRemote)
		{
			EntityLivingBase living = (EntityLivingBase)event.entity;
		
			if(living instanceof EntityWolf && ((EntityWolf)living).isTamed())
			{
				setMaxHealth(living,heartsWolfTamed*2);
			}
			if(living instanceof EntityOcelot && ((EntityOcelot)living).isTamed())
			{
				setMaxHealth(living,heartsCatTamed*2);
			}
			
			if(living instanceof EntityVillager && ((EntityVillager)living).isChild() == false)
			{
				setMaxHealth(living,heartsVillager*2);			
			}
		}
	}*/

}
