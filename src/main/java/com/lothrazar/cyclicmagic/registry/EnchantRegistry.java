package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.enchantment.EnchantLaunch;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class EnchantRegistry {

	public static EnchantLaunch launch;
	//TODO: config this in modpack area to avoid conflicts. min 71
	public static int launchid;

	public static void register(){
		launch = new EnchantLaunch();
		
		Enchantment.REGISTRY.register(launchid, new ResourceLocation(launch.getName()), launch);   
	}

	public static void syncConfig(Configuration c) {
		launchid = c.getInt("enchant.launchid", Const.ConfigCategory.modpackMisc, 
				86, 71, 999, "Id of the launch enchantment (double jump on boots).  Change this if you get id conflicts with other mods.");
		
	}
}
