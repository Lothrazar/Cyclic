package com.lothrazar.cyclicmagic.registry;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class StackSizeRegistry {

	public static boolean enabled;

	public static void register() {

		Map<Item, Integer> stackMap = new HashMap<Item, Integer>();
		stackMap.put(Items.potionitem, 8);
		stackMap.put(Items.splash_potion, 8);
		stackMap.put(Items.lingering_potion, 8);
		int boat = 16;

		stackMap.put(Items.boat, 64);
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
		stackMap.put(Items.snowball, 64);
		stackMap.put(Items.banner, 64);
		stackMap.put(Items.snowball, 64);
		stackMap.put(Items.armor_stand, 64);
		stackMap.put(Items.sign, 64);
		stackMap.put(Items.bed, 64);
		stackMap.put(Items.bucket, 64);

		for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {

			entry.getKey().setMaxStackSize(entry.getValue());
		}
	}

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Stack Size";

		// config.setCategoryComment(category, "Tons of new recipes for existing
		// blocks and items. Bonemeal to undye wool; repeater and dispenser tweaks;
		// making player skulls out of the four mob heads...");

		Property prop = config.get(category, "enabled", true, "Increase stack size of many vanilla items");
		prop.setRequiresWorldRestart(true);
		enabled = prop.getBoolean();
	}
}
