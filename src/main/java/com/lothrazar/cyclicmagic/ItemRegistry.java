package com.lothrazar.cyclicmagic;

import java.util.ArrayList; 
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemRespawnEggAnimal;
import com.lothrazar.cyclicmagic.item.*; 
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry 
{ 
	public static ArrayList<Item> items = new ArrayList<Item>();
    
	
	public static ItemRespawnEggAnimal respawn_egg; 
	
	
	//public static ItemRespawnEggAnimal respawn_egg; //TODO: make standalone mod for respawn_eggs
	
	public static Item exp_cost_dummy;
	public static Item exp_cost_empty_dummy;
	public static Item spell_dummy_deposit;
	public static ItemChestSack itemChestSack;  
	public static Item spell_ghost_dummy;
	public static Item spell_haste_dummy; 
	public static Item spell_jump_dummy;
	public static Item spell_dummy_phasing;
	public static Item spell_dummy_slowfall;
	public static Item spell_waterwalk_dummy;
	
	public static void register()
	{   
		ItemRegistry.itemChestSack = new ItemChestSack();   
		ItemRegistry.registerItem(ItemRegistry.itemChestSack, "chest_sack");
	
		respawn_egg = new ItemRespawnEggAnimal();
		ItemRegistry.registerItem(respawn_egg, "respawn_egg");
		
		spell_dummy_deposit = new Item();
		ItemRegistry.registerItem(spell_dummy_deposit, "spell_dummy_deposit");
		spell_dummy_slowfall = new Item();
		ItemRegistry.registerItem(spell_dummy_slowfall, "spell_dummy_slowfall");
		spell_dummy_phasing = new Item();
		ItemRegistry.registerItem(spell_dummy_phasing, "spell_dummy_phasing");
		exp_cost_dummy = new Item();
		ItemRegistry.registerItem(exp_cost_dummy, "exp_cost_dummy");
		exp_cost_empty_dummy = new Item();
		ItemRegistry.registerItem(exp_cost_empty_dummy, "exp_cost_empty_dummy");
		spell_jump_dummy = new Item();
		ItemRegistry.registerItem(spell_jump_dummy, "spell_jump_dummy");
		spell_waterwalk_dummy = new Item();
		ItemRegistry.registerItem(spell_waterwalk_dummy, "spell_waterwalk_dummy");
		spell_ghost_dummy = new Item();
		ItemRegistry.registerItem(spell_ghost_dummy, "spell_ghost_dummy");
		spell_haste_dummy = new Item();
		ItemRegistry.registerItem(spell_haste_dummy, "spell_haste_dummy");
		
	}
	
	public static void registerItem(Item item, String name)
	{ 
		 item.setUnlocalizedName(name);
		 
		 GameRegistry.registerItem(item, name);
		 
		 items.add(item);
	}
}
