package com.lothrazar.cyclicmagic;

import java.util.ArrayList; 
import com.lothrazar.cyclicmagic.item.*; 
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry 
{ 
	public static ArrayList<Item> items = new ArrayList<Item>();
    
	
	public static ItemRespawnEggAnimal respawn_egg; 
	
	
	//public static ItemRespawnEggAnimal respawn_egg; //TODO: make standalone mod for respawn_eggs
	
	public static ItemChestSack itemChestSack;  
	
	public static void register()
	{   
		ItemRegistry.itemChestSack = new ItemChestSack();   
		ItemRegistry.registerItem(ItemRegistry.itemChestSack, "chest_sack");
	
		respawn_egg = new ItemRespawnEggAnimal();
		ItemRegistry.registerItem(respawn_egg, "respawn_egg");

		
	}
	
	public static void registerItem(Item item, String name)
	{ 
		 item.setUnlocalizedName(name);
		 
		 GameRegistry.registerItem(item, name);
		 
		 items.add(item);
	}
}
