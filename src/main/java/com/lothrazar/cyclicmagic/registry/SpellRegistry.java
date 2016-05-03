package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.*;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class SpellRegistry {
	public static boolean								renderOnLeft;

	private static Map<Integer, ISpell>	hashbook;
	// TODO: move from ints to strings one day..??
	// private static Map<String, ISpell> spellRegistry;

	public static class Spells {

		// on purpose, not all spells are in here. only ones that needed to be
		// exposed
		public static SpellRangeRotate	rotate;
		public static SpellRangePush		push;
		public static SpellRangePull		pull;
		public static SpellRangeReplace	replacer;
		private static SpellInventory		inventory;
		private static SpellRangeBuild		reachdown;
		private static SpellLaunch				launch;
		private static SpellRangeBuild		reachup;
		private static SpellRangeBuild		reachplace;
		private static SpellPlaceLine		placeline;
		private static SpellPlaceCircle	placecircle;
		private static SpellPlaceStair		placestair;
		// public static SpellPlaceFloor placefloor;
	}

	public static void register() {

	 
		ArrayList<ISpell>	spellbookBuild = new ArrayList<ISpell>();
		ArrayList<ISpell>	spellbookNoInvo = new ArrayList<ISpell>();
		
		hashbook = new HashMap<Integer, ISpell>();
		// spellRegistry = new HashMap<String, ISpell>();

		int spellId = -1;// the smallest spell gets id zero
		
		Spells.inventory = new SpellInventory(++spellId, "inventory");
		registerSpell(Spells.inventory);
		spellbookBuild.add(Spells.inventory);
		
		Spells.rotate = new SpellRangeRotate(++spellId, "rotate");
		registerSpell(Spells.rotate);
		spellbookNoInvo.add(Spells.rotate);

		Spells.push = new SpellRangePush(++spellId, "push");
		registerSpell(Spells.push);
		spellbookNoInvo.add(Spells.push);

		Spells.pull = new SpellRangePull(++spellId, "pull");
		registerSpell(Spells.pull);
		spellbookNoInvo.add(Spells.pull);

		Spells.replacer = new SpellRangeReplace(++spellId, "replacer");
		registerSpell(Spells.replacer);
		spellbookBuild.add(Spells.replacer);

		Spells.reachup = new SpellRangeBuild(++spellId, "reachup", SpellRangeBuild.PlaceType.UP);
		registerSpell(Spells.reachup);
		spellbookBuild.add(Spells.reachup);

		Spells.reachplace = new SpellRangeBuild(++spellId, "reachplace", SpellRangeBuild.PlaceType.PLACE);
		registerSpell(Spells.reachplace);
		spellbookBuild.add(Spells.reachplace);

		Spells.reachdown = new SpellRangeBuild(++spellId, "reachdown", SpellRangeBuild.PlaceType.DOWN);
		registerSpell(Spells.reachdown);
		//spellbookBuild.add(Spells.placestair);

		//TODO: currently there is no tool for this
		// it would not have the BUILD TOGGLE TYPE.. once its working
		Spells.placeline = new SpellPlaceLine(++spellId, "placeline");
		registerSpell(Spells.placeline);

		Spells.placecircle = new SpellPlaceCircle(++spellId, "placecircle");
		registerSpell(Spells.placecircle);

		Spells.placestair = new SpellPlaceStair(++spellId, "placestair");
		registerSpell(Spells.placestair);

		

		ArrayList<ISpell>		 spellbookFly = new ArrayList<ISpell>();

		Spells.launch = new SpellLaunch(++spellId, "launch");
		registerSpell(Spells.launch);
		spellbookFly.add(Spells.launch);
		
		if(ItemRegistry.cyclic_wand_fly != null){
			ItemRegistry.cyclic_wand_fly.setSpells(spellbookFly);
		}
		if(ItemRegistry.cyclic_wand_range != null){
			ItemRegistry.cyclic_wand_range.setSpells(spellbookNoInvo);
		}
		if(ItemRegistry.cyclic_wand_build != null){
			ItemRegistry.cyclic_wand_build.setSpells(spellbookBuild);
		}
		
		
		// Spells.placefloor = new SpellPlaceFloor(++spellId, "placefloor");
		// registerBuildSpell(Spells.placefloor);
	}
 
	private static void registerSpell(ISpell spell) {

		hashbook.put(spell.getID(), spell);
		// spellRegistry.put(spell.getUnlocalizedName(), spell);
	}

	public static boolean spellsEnabled(EntityPlayer player) {
		// current requirement is only a wand
		return UtilSpellCaster.getPlayerWandIfHeld(player) != null;
	}

	public static ISpell getSpellFromID(int id) {

		if (hashbook.containsKey(id)) { return hashbook.get(id); }

		return null;
	}

	public static void syncConfig(Configuration config) {

	}

	public static List<ISpell> getSpellbook(ItemStack wand) {
		return ((ItemCyclicWand)wand.getItem()).getSpells();
	}
	public static ISpell next(ItemStack wand, ISpell spell) {

		List<ISpell> book = getSpellbook(wand);

		int indexCurrent = book.indexOf(spell);

		int indexNext = indexCurrent + 1;
		if (indexNext >= book.size()) {
			indexNext = 0;
		}

		return book.get(indexNext);
		// SpellRegistry.getSpellFromID(indexNext);
	}

	public static ISpell prev(ItemStack wand, ISpell spell) {

		List<ISpell> book = getSpellbook(wand);

		int indexCurrent = book.indexOf(spell);
		int indexPrev;

		if (indexCurrent <= 0)// not that it ever WOULD be.. negative.. yeah
			indexPrev = book.size() - 1;
		else
			indexPrev = indexCurrent - 1;

		ISpell ret = book.get(indexPrev);

		return ret;// SpellRegistry.getSpellFromID(indexPrev);

	}
	/*
	 * private static int nextId(ItemStack stack, int spell_id){
	 * 
	 * int next;
	 * 
	 * if(spell_id >= SpellRegistry.getSpellbook(stack).size() - 1)
	 * next = 0;// (int)spells[0];
	 * else
	 * next = spell_id + 1;// (int)spells[spell_id+1];
	 * 
	 * return next;
	 * }
	 * 
	 * private static int prevId(ItemStack stack, int spell_id){
	 * 
	 * int prev;
	 * 
	 * if(spell_id == 0)
	 * prev = SpellRegistry.getSpellbook(stack).size() - 1;
	 * else
	 * prev = spell_id - 1;
	 * 
	 * return prev;
	 * }
	 */

}
