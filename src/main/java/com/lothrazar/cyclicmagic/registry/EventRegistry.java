package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.lothrazar.cyclicmagic.event.*;

public class EventRegistry{
	
	private static ArrayList<Object> events = new ArrayList<Object>();

	private static boolean enderDropBlock = true;

	private static boolean nameTagDeath;

	//private static boolean nameVillager;

	private static boolean playerWakeup;

	private static boolean signSkullName;

	private static boolean playerDeathCoords;
	public static void syncConfig(Configuration config){

		String category = "mobs";
		
		enderDropBlock = config.getBoolean("enderDropBlock", category, true, "Drop carried block on death");

		nameTagDeath = config.getBoolean("nameTagDeath", category, true, "");

		//nameVillager = config.getBoolean("nameVillager", category, true, "");


		enderDropBlock = config.getBoolean("enderDropBlock", category, true, "");
		
		signSkullName = config.getBoolean("signSkullName", category, true, "");
		
		playerDeathCoords = config.getBoolean("playerDeathCoords", category, true, "");
		

		category = "player";

		playerWakeup = config.getBoolean("playerWakeup", category, true, "");
	}

	public static void register(){
		//some just always have to happen
		events.add(new EventConfigChanged());
		events.add(new EventPotions());
		events.add(new EventSpells());
		
		//some events are featured that get configured
		
		if(enderDropBlock)
			events.add(new EventEndermanDropBlock());
		
		//TODO: decide if we split this event out for each mob or something?
		events.add(new EventMobDrops());
		
		if(nameTagDeath)
			events.add(new EventNametagDeath());
		
		//events.add(new EventNameVillager());
		
		if(playerDeathCoords)
			events.add(new EventPlayerDeathCoords());
		
		if(playerWakeup)
			events.add(new EventPlayerWakeup());
		
		if(signSkullName)
			events.add(new EventSignSkullName());
		
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
