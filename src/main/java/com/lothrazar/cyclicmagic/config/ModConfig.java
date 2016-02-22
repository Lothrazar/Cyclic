package com.lothrazar.cyclicmagic.config;

import java.util.ArrayList;
import java.util.Collections;
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
	public int maxEmerald;
	public int maxQuartz;
	public int maxLapis;
	public int maxDiamond;
	public int maxRedstone;
	public int maxGold;
	public Integer maxLargestForManabar;

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

		potionIdMagnet = instance.get(category, "magnet_id", 43).getInt();
		
		category = "maximum_energy";
		int min = 100;
		int max = 2000;
		maxQuartz   = instance.getInt("max_quartz",  category,  500,min,max,"");
		maxLapis    = instance.getInt("max_lapis",   category,  600,min,max,"");
		maxGold     = instance.getInt("max_gold",    category,  700,min,max,"");
		maxRedstone = instance.getInt("max_redstone",category,  800,min,max,"");
		maxDiamond  = instance.getInt("max_diamond", category,  900,min,max,"");
		maxEmerald  = instance.getInt("max_emerald", category,  1000,min,max,"");
		

		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(maxDiamond);
		list.add(maxEmerald);
		list.add(maxGold);
		list.add(maxLapis);
		list.add(maxQuartz);
		list.add(maxRedstone);
		maxLargestForManabar = Collections.max(list);
		
		category = "effect_tweaks";

		slowfallSpeed = instance.getFloat("slowfall_speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

		if(instance.hasChanged()){
			instance.save();
		}
	}
}
