package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.*; 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SpellRegistry{

	private static ArrayList<ISpell> spellbook;
	private static HashMap<Integer, ISpell> hashbook; 


	static SpellScreenRender screen;
	public static SpellCaster caster;

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

		screen = new SpellScreenRender();
		caster = new SpellCaster();
		spellbook = new ArrayList<ISpell>();
		hashbook = new HashMap<Integer, ISpell>();
		  
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
	}

	public static ISpell getDefaultSpell(){

		return getSpellFromID(0);
	}

	public static boolean spellsEnabled(EntityPlayer player){
		//current requirement is only a wand
		return SpellCaster.getPlayerWandIfHeld(player) != null;
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
}
