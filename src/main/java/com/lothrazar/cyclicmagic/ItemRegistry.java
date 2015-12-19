package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
	public static ArrayList<Item> items = new ArrayList<Item>();

	public static ItemRespawnEggAnimal respawn_egg;
	public static ItemChestSack chest_sack;
	public static MasterWand master_wand;
	public static ItemWandLaunch launch_wand;

	public static void register() {
		chest_sack = new ItemChestSack();
		ItemRegistry.registerItem(chest_sack, "chest_sack");

		respawn_egg = new ItemRespawnEggAnimal();
		ItemRegistry.registerItem(respawn_egg, "respawn_egg");
		
		master_wand = new MasterWand();
		ItemRegistry.registerItem(master_wand, "master_wand");

		launch_wand = new ItemWandLaunch();
		ItemRegistry.registerItem(launch_wand, "launch_wand");
		
		GameRegistry.addRecipe(new ItemStack(master_wand), //placeholder rec
				"xxx",
				"xxx",
				"xxx",
				'x',Items.leather);
	}

	public static void registerItem(Item item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		items.add(item);
	}
}
