package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
	public static ArrayList<Item> items = new ArrayList<Item>();

	public static ItemRespawnEggAnimal respawn_egg;
	public static ItemChestSack chest_sack;
	public static Item master_wand;

	public static void register() {
		chest_sack = new ItemChestSack();
		ItemRegistry.registerItem(chest_sack, "chest_sack");

		respawn_egg = new ItemRespawnEggAnimal();
		ItemRegistry.registerItem(respawn_egg, "respawn_egg");
		
		master_wand = new Item();
		ItemRegistry.registerItem(master_wand, "master_wand");
	}

	public static void registerItem(Item item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		items.add(item);
	}
}
