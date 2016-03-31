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

	public static ItemCyclicWand cyclic_wand;

	public static void register(){

		cyclic_wand = new ItemCyclicWand();
		registerItem(cyclic_wand, "cyclic_wand");

		GameRegistry.addRecipe(new ItemStack(cyclic_wand), 
				"sds", 
				" o ", 
				"gog", 
				'd', new ItemStack(Blocks.diamond_block), 
				'g', Items.ghast_tear, 
				'o', Blocks.obsidian, 
				's', Items.nether_star);
		
		Item multitool = new ItemMultiTool();
		registerItem(multitool,"multitool");
		
		Item carbon_paper = new ItemPaperCarbon();
		registerItem(carbon_paper,"carbon_paper");
		
		GameRegistry.addRecipe(new ItemStack(carbon_paper,8),
				"ppp",
				"pcp",
				"ppp",
				'c',new ItemStack(Items.coal,1,1), //charcoal
				'p',Items.paper  );
	}

	public static void registerItem(Item item, String name){

		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		items.add(item);
	}
}
