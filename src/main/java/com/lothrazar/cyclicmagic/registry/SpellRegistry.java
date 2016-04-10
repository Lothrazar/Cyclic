package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.spell.*;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class SpellRegistry {
	public static boolean								renderOnLeft;
	private static ArrayList<ISpell>		spellbookRange;
	private static ArrayList<ISpell>		spellbookBuild;
	private static ArrayList<ISpell>		spellbookFly;
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
		public static SpellInventory		inventory;
		public static SpellRangeBuild		reachdown;

		public static SpellLaunch				launch;

		public static SpellRangeBuild		reachup;
		public static SpellRangeBuild		reachplace;
		public static SpellPlaceLine		placeline;
		public static SpellPlaceCircle	placecircle;
		public static SpellPlaceStair		placestair;
		// public static SpellPlaceFloor placefloor;
	}

	public static void register() {

		// spellbook = new ArrayList<ISpell>();
		spellbookRange = new ArrayList<ISpell>();
		spellbookBuild = new ArrayList<ISpell>();
		spellbookFly = new ArrayList<ISpell>();
		hashbook = new HashMap<Integer, ISpell>();
		// spellRegistry = new HashMap<String, ISpell>();

		int spellId = -1;// the smallest spell gets id zero

		Spells.replacer = new SpellRangeReplace(++spellId, "replacer");
		registerReachSpell(Spells.replacer);

		Spells.rotate = new SpellRangeRotate(++spellId, "rotate");
		registerReachSpell(Spells.rotate);

		Spells.inventory = new SpellInventory(++spellId, "inventory");
		registerSpell(Spells.inventory);// special case where its in both
		spellbookRange.add(Spells.inventory);
		spellbookBuild.add(Spells.inventory);

		Spells.push = new SpellRangePush(++spellId, "push");
		registerReachSpell(Spells.push);

		Spells.pull = new SpellRangePull(++spellId, "pull");
		registerReachSpell(Spells.pull);

		Spells.launch = new SpellLaunch(++spellId, "launch");
		registerSpell(Spells.launch);
		spellbookFly.add(Spells.launch);

		Spells.reachup = new SpellRangeBuild(++spellId, "reachup", SpellRangeBuild.PlaceType.UP);
		registerReachSpell(Spells.reachup);

		Spells.reachplace = new SpellRangeBuild(++spellId, "reachplace", SpellRangeBuild.PlaceType.PLACE);
		registerReachSpell(Spells.reachplace);

		Spells.reachdown = new SpellRangeBuild(++spellId, "reachdown", SpellRangeBuild.PlaceType.DOWN);
		registerReachSpell(Spells.reachdown);

		Spells.placeline = new SpellPlaceLine(++spellId, "placeline");
		registerBuildSpell(Spells.placeline);

		Spells.placecircle = new SpellPlaceCircle(++spellId, "placecircle");
		registerBuildSpell(Spells.placecircle);

		Spells.placestair = new SpellPlaceStair(++spellId, "placestair");
		registerBuildSpell(Spells.placestair);

		// Spells.placefloor = new SpellPlaceFloor(++spellId, "placefloor");
		// registerBuildSpell(Spells.placefloor);
	}

	private static void registerBuildSpell(ISpell spell) {
		registerSpell(spell);
		spellbookBuild.add(spell);
	}

	private static void registerReachSpell(ISpell spell) {
		registerSpell(spell);
		spellbookRange.add(spell);
	}

	private static void registerSpell(ISpell spell) {

		hashbook.put(spell.getID(), spell);
		// spellRegistry.put(spell.getUnlocalizedName(), spell);
	}

	public static ISpell getDefaultSpell() {

		return getSpellFromID(0);
	}

	public static boolean spellsEnabled(EntityPlayer player) {
		// current requirement is only a wand
		return UtilSpellCaster.getPlayerWandIfHeld(player) != null;
	}

	public static ISpell getSpellFromID(int id) {

		if (hashbook.containsKey(id)) { return hashbook.get(id); }

		return null;
	}

	public static ArrayList<ISpell> getSpellbook(ItemStack wand) {
		if (wand.getItem() == ItemRegistry.cyclic_wand) {
			return spellbookRange;
		}
		if (wand.getItem() == ItemRegistry.cyclic_wand_range) { 
			return spellbookBuild; 
		}

		if (wand.getItem() == ItemRegistry.cyclic_wand_fly) { 
			return spellbookFly; 
		}
		
		return null;
	}

	public static void syncConfig(Configuration config) {

		String category = "";
		category = Const.MODCONF + "Spells";

		SpellRegistry.renderOnLeft = config.getBoolean("HUD on left", category, true, "True for top left of the screen, false for top right");

		BaseSpellRange.maxRange = config.getInt("Max Range", category, 64, 8, 128, "Maximum range for all spells");

	}

	public static ISpell next(ItemStack wand, ISpell spell) {

		ArrayList<ISpell> book = SpellRegistry.getSpellbook(wand);

		int indexCurrent = book.indexOf(spell);

		int indexNext = indexCurrent + 1;
		if (indexNext >= book.size()) {
			indexNext = 0;
		}

		return book.get(indexNext);
		// SpellRegistry.getSpellFromID(indexNext);
	}

	public static ISpell prev(ItemStack wand, ISpell spell) {

		ArrayList<ISpell> book = SpellRegistry.getSpellbook(wand);

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
