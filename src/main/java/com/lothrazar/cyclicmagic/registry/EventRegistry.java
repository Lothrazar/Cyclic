package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.lothrazar.cyclicmagic.event.*;

public class EventRegistry{
	
	private static ArrayList<Object> events = new ArrayList<Object>();

	private static boolean nameTagDeath;

	private static boolean playerWakeup;

	private static boolean signSkullName;

	private static boolean playerDeathCoords;
	public static void syncConfig(Configuration config){

		String category = "mobs";
		
		nameTagDeath = config.getBoolean("nameTagDeath", category, true, "When an entity dies that is named with a tag, it drops the nametag");

		category = "items";

		signSkullName = config.getBoolean("signSkullName", category, true, "Use a player skull on a sign to name the skull based on the top line");

		category = "player";

		playerWakeup = config.getBoolean("playerWakeup", category, true, "Using a bed to skip the night has some mild potion effect related drawbacks");
		
		playerDeathCoords = config.getBoolean("playerDeathCoords", category, true, "Display your coordinates in chat when you die");
	}

	public static void register(){
		//some just always have to happen no matter what. for other features.
		events.add(new EventConfigChanged());
		events.add(new EventPotions());
		events.add(new EventSpells());
		
		//no reason to turn these off
		events.add(new EventNameVillager());
		events.add(new EventEndermanDropBlock());
		
		//some events are featured that get configured
		
		
		//TODO: decide if we split this event out for each mob or something? and figure configs out
		events.add(new EventAnimalDropBuffs());
		events.add(new EventMobDropsReduced());
		
		if(nameTagDeath)
			events.add(new EventNametagDeath());

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
