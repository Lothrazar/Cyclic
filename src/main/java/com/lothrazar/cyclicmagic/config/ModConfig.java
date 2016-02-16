package com.lothrazar.cyclicmagic.config;

import com.lothrazar.cyclicmagic.Const;
import net.minecraftforge.common.config.Configuration;

public class ModConfig{

	private Configuration instance;
	private String category = "";

	public int potionIdWaterwalk;
	public int potionIdSlowfall;
	public int potionIdFrost;
	public int potionIdMagnet;
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

		category = "effect_ids";

		instance.addCustomCategoryComment(category, "IDs are only exposed to avoid conflicts with other mods.  Messing with these might break the game.   ");

		potionIdWaterwalk = instance.get(category, "waterwalk_id", 40).getInt();

		potionIdSlowfall = instance.get(category, "slowfall_id", 41).getInt();

		potionIdFrost = instance.get(category, "frost_id", 42).getInt();

		potionIdMagnet = instance.get(category, "magnet_", 43).getInt();

		category = "effect_tweaks";

		slowfallSpeed = instance.getFloat("slowfall_speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

		if(instance.hasChanged()){
			instance.save();
		}
	}
}
