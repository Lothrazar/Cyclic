package com.lothrazar.samsmagic;

import net.minecraftforge.common.config.Configuration;

public class ModConfig
{ 
	private Configuration instance;
	private String category = "";
	public int deposit;
	public int chesttransp;
	public int ghost;
	public int jump;
	public int phase;
	public int slowfall;
	public int waterwalk;
	public int haste;
	
	public int potionIdWaterwalk;
	public int potionIdSlowfall;
	public float slowfallSpeed; 
	
	public Configuration instance()
	{
		return instance;
	}
	
	public ModConfig(Configuration c)
	{
		instance = c; 
		instance.load();
 
		category = Const.MODID;
		instance.addCustomCategoryComment(category, 
				"The number is the EXP cost of the spell.  Set to -1 to disable the spell.   ");
		
		deposit = instance.get(category,"deposit",  5).getInt();
		chesttransp = instance.get(category,"chest_transport", 5).getInt();
		ghost = instance.get(category,"ghost",   50).getInt();
		jump = instance.get(category,"jump",  10).getInt();
		phase = instance.get(category,"phase",  25).getInt();
		slowfall = instance.get(category,"slowfall",  15).getInt();
		waterwalk = instance.get(category,"waterwalk",  15).getInt();

		haste = instance.get(category,"haste",  20).getInt();
	
		category = "effect_ids";

		instance.addCustomCategoryComment(category, 
				"IDs are only exposed to avoid conflicts with other mods.  Messing with these might break the game.   ");
		
		potionIdWaterwalk = instance.get(category,"waterwalk_id", 40).getInt();
		  
		potionIdSlowfall = instance.get(category,"slowfall_id", 41).getInt();
	
		category = "effect_tweaks";
		
		slowfallSpeed = instance.getFloat("slowfall_speed",category, 0.41F,0.1F,1F,
    			"This factor affects how much the slowfall effect slows down the entity.");
 
		//EntitySnowballBolt.secondsFrozenOnHit = instance.getInt("frost_duration_on_hit",category, 25,1,600,
    	//		"When something hit by one of these snowballs, it gets the snow effect for this many seconds.");

		if(instance.hasChanged()){ instance.save(); }
 
	}
}
