package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry{

	public static ArrayList<Item> items = new ArrayList<Item>();

	public static ItemRespawnEggAnimal respawn_egg;
	public static ItemChestSack chest_sack;
	public static ItemCyclicWand cyclic_wand;
	public static ItemPaperCarbon carbon_paper;
	public static ItemWaypointPortal waypoint_portal;

	public static void register(){

		chest_sack = new ItemChestSack();
		ItemRegistry.registerItem(chest_sack, "chest_sack");

		respawn_egg = new ItemRespawnEggAnimal();
		ItemRegistry.registerItem(respawn_egg, "respawn_egg");

		carbon_paper = new ItemPaperCarbon();
		ItemRegistry.registerItem(carbon_paper, "carbon_paper");

		waypoint_portal = new ItemWaypointPortal();
		ItemRegistry.registerItem(waypoint_portal, "waypoint_portal");

		cyclic_wand = new ItemCyclicWand();
		cyclic_wand.setUnlocalizedName("cyclic_wand");
		GameRegistry.registerItem(cyclic_wand, "cyclic_wand");

		GameRegistry.addRecipe(new ItemStack(cyclic_wand, 1, ItemCyclicWand.Variant.QUARTZ.ordinal()), 
				"sds", 
				" o ", 
				"gog", 'd', Blocks.quartz_block, 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

		GameRegistry.addRecipe(new ItemStack(cyclic_wand, 1, ItemCyclicWand.Variant.GOLD.ordinal()), 
				"sds", 
				" o ", 
				"gog", 'd', Blocks.gold_block, 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);

		GameRegistry.addRecipe(new ItemStack(cyclic_wand, 1, ItemCyclicWand.Variant.LAPIS.ordinal()), 
				"sds", 
				" o ", 
				"gog", 'd', Blocks.lapis_block, 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
		
		GameRegistry.addRecipe(new ItemStack(cyclic_wand, 1, ItemCyclicWand.Variant.DIAMOND.ordinal()), 
				"sds", 
				" o ", 
				"gog", 'd', Blocks.diamond_block, 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
		
		GameRegistry.addRecipe(new ItemStack(cyclic_wand, 1, ItemCyclicWand.Variant.EMERALD.ordinal()), 
				"sds", 
				" o ", 
				"gog", 'd', Blocks.emerald_block, 'g', Items.ghast_tear, 'o', Blocks.obsidian, 's', Items.nether_star);
	}

	public static void registerItem(Item item, String name){

		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		items.add(item);
	}
}
