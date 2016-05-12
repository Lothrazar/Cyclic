package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class StackSizeRegistry {

	public static boolean enabled=true;
	public static Map<Item, Integer> stackMap = new HashMap<Item, Integer>();
	
	
	public static void register() {
		if(enabled == false){
			return;
		}

		stackMap.put(Items.potionitem, 8);
		stackMap.put(Items.splash_potion, 8);
		stackMap.put(Items.lingering_potion, 8);
		int boat = 16;
		int MAX = 64;

		stackMap.put(Items.boat, boat);
		stackMap.put(Items.acacia_boat, boat);
		stackMap.put(Items.birch_boat, boat);
		stackMap.put(Items.spruce_boat, boat);
		stackMap.put(Items.dark_oak_boat, boat);
		stackMap.put(Items.jungle_boat, boat);
		stackMap.put(Items.minecart, boat);
		stackMap.put(Items.chest_minecart, boat);
		stackMap.put(Items.furnace_minecart, boat);
		stackMap.put(Items.hopper_minecart, boat);
		stackMap.put(Items.tnt_minecart, boat);
		stackMap.put(Items.snowball, MAX);
		stackMap.put(Items.banner, MAX);
		stackMap.put(Items.snowball, MAX);
		stackMap.put(Items.armor_stand, MAX);
		stackMap.put(Items.sign, MAX);
		stackMap.put(Items.bed, MAX);
		stackMap.put(Items.bucket, MAX);

		for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {

			entry.getKey().setMaxStackSize(entry.getValue());
		}
	}

	public static void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.blockChanges;

		// config.setCategoryComment(category, "Tons of new recipes for existing
		// blocks and items. Bonemeal to undye wool; repeater and dispenser tweaks;
		// making player skulls out of the four mob heads...");

		Property prop = config.get(category, "StackSizeEnabled", true, "Increase stack size of many vanilla items");
		prop.setRequiresWorldRestart(true);
		enabled = prop.getBoolean();
	}
}
