package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


public class StackSizeRegistry{

	
	public static void register(){

		ArrayList<Item> stackTo8 = new ArrayList<Item>();
		stackTo8.add(Items.potionitem);
		
		//TODO: could do a hashmap => stacksize thing instead
		ArrayList<Item> stackTo64 = new ArrayList<Item>();
		
		stackTo64.add(Items.boat);
		stackTo64.add(Items.acacia_boat);
		stackTo64.add(Items.birch_boat);
		stackTo64.add(Items.spruce_boat);
		stackTo64.add(Items.dark_oak_boat);
		stackTo64.add(Items.jungle_boat);
		
		for(Item item : stackTo64){
			item.setMaxStackSize(64); 
		}

		ArrayList<Item> stackTo16 = new ArrayList<Item>();
		
	}
}
