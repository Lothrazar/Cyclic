package com.lothrazar.cyclicmagic.config;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const;

public class ModConfig{


	public static void syncConfig(){

		String category = "";
		category = Const.MODID;

		SpellRegistry.renderOnLeft = ModMain.config.getBoolean("on_left", category, true, "True for top left of the screen, false for top right");

		category = "effect_tweaks";

		PotionRegistry.slowfallSpeed = ModMain.config.getFloat("slowfall_speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

		//if(instance.hasChanged()){
		ModMain.config.save();
		//}
	}
}
