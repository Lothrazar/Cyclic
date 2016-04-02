package com.lothrazar.cyclicmagic.config;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class ModConfig{

	private Configuration instance;
	private String category = "";

	//public int potionIdSlowfall;
	//public int potionIdMagnet;
	public float slowfallSpeed;
	public boolean renderOnLeft;
 
	public Configuration instance(){

		return instance;
	}

	public ModConfig(Configuration c){

		instance = c;
		instance.load();

		syncConfig();
	}

	public void syncConfig(){

		category = Const.MODID;

		renderOnLeft = instance.getBoolean("on_left", category, true, "True for top left of the screen, false for top right");

		category = "effect_tweaks";

		slowfallSpeed = instance.getFloat("slowfall_speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

		//if(instance.hasChanged()){
			instance.save();
		//}
	}
}
