package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.spell.*; 
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class SpellRegistry{
	public static boolean renderOnLeft;
	private static ArrayList<ISpell> spellbook;
	private static Map<Integer, ISpell> hashbook; 
	private static Map<String, ISpell> spellRegistry; 


	public static class Spells{

		//on purpose, not all spells are in here. only ones that needed to be exposed
		public static SpellRangeRotate rotate;
		public static SpellRangePush push;
		public static SpellRangePull pull;
		public static SpellRangeReplace replacer;
		public static SpellInventory inventory;
		public static SpellRangeBuild reachdown;

		public static SpellLaunch launch;

		public static SpellRangeBuild reachup;
		public static SpellRangeBuild reachplace;
	}

	public static void register(){

		spellbook = new ArrayList<ISpell>();
		hashbook = new HashMap<Integer, ISpell>();
		spellRegistry = new HashMap<String, ISpell>();
		  
		int spellId = -1;// the smallest spell gets id zero

		Spells.replacer = new SpellRangeReplace(++spellId, "replacer");
		registerSpell(Spells.replacer);

		Spells.rotate = new SpellRangeRotate(++spellId, "rotate");
		registerSpell(Spells.rotate);
		
		Spells.inventory = new SpellInventory(++spellId, "inventory");
		registerSpell(Spells.inventory);

		Spells.push = new SpellRangePush(++spellId, "push");
		registerSpell(Spells.push);

		Spells.pull = new SpellRangePull(++spellId, "pull");
		registerSpell(Spells.pull);

		Spells.launch = new SpellLaunch(++spellId, "launch");
		registerSpell(Spells.launch);

		Spells.reachup = new SpellRangeBuild(++spellId, "reachup",SpellRangeBuild.PlaceType.UP);
		registerSpell(Spells.reachup);
		
		Spells.reachplace = new SpellRangeBuild(++spellId, "reachplace",SpellRangeBuild.PlaceType.PLACE);
		registerSpell(Spells.reachplace);

		Spells.reachdown = new SpellRangeBuild(++spellId, "reachdown",SpellRangeBuild.PlaceType.DOWN);
		registerSpell(Spells.reachdown);
	}

	private static void registerSpell(ISpell spell){

		spellbook.add(spell);
		hashbook.put(spell.getID(), spell);
		spellRegistry.put(spell.getUnlocalizedName(), spell);
	}

	public static ISpell getDefaultSpell(){

		return getSpellFromID(0);
	}

	public static boolean spellsEnabled(EntityPlayer player){
		//current requirement is only a wand
		return UtilSpellCaster.getPlayerWandIfHeld(player) != null;
	}

	public static ISpell getSpellFromID(int id){

		if(hashbook.containsKey(id)){
			return hashbook.get(id);
		}
		
		return null;
	}

	public static ArrayList<ISpell> getSpellbook(){

		return spellbook;
	}

	public static void syncConfig(Configuration config){

		String category = "";
		category = Const.MODCONF+"gui";

		SpellRegistry.renderOnLeft = config.getBoolean("spell_gui_on_left", category, true, "True for top left of the screen, false for top right");

	}
	
	
	
}
