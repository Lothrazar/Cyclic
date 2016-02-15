package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import java.util.HashMap;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.*;
import com.lothrazar.cyclicmagic.spell.passive.IPassiveSpell;
import com.lothrazar.cyclicmagic.spell.passive.PassiveBreath;
import com.lothrazar.cyclicmagic.spell.passive.PassiveBurn;
import com.lothrazar.cyclicmagic.spell.passive.PassiveDefend;
import com.lothrazar.cyclicmagic.spell.passive.PassiveFalling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class SpellRegistry{

	private static ArrayList<ISpell> spellbook;
	private static HashMap<Integer, ISpell> hashbook;
	private static HashMap<Integer, IPassiveSpell> passives;

	static SpellScreenRender screen;
	public static SpellCaster caster;

	public static class Spells{

		//on purpose, not all spells are in here. only ones that needed to be exposed
		public static SpellRangeRotate rotate;
		public static SpellRangePush push;
		public static SpellRangePull pull;
		public static SpellRangeReplace replacer;
		public static SpellInventory inventory;
		public static SpellRangeBuild reach;
		public static SpellPotion haste;
		public static SpellChestSack chestsack;
		public static SpellThrowTorch torch;
		public static SpellPotion waterwalk;
		public static ISpell phase;
		public static ISpell ghost;
		public static SpellPotion nightvision;
		public static SpellThrowFishing fishing;
		public static ISpell ice;
		public static ISpell shear;
		public static SpellPotion magnet;
		public static SpellLaunch launch;
		public static SpellCarbonPaper carbon;
		public static SpellScaffolding scaffold;
		public static SpellThrowSpawnEgg spawnegg;
		public static SpellThrowLightning lightning;
		public static SpellLinkingPortal waypoint;
	}
	
	public static class Passives{
		
		private static void register(){
			passives = new HashMap<Integer, IPassiveSpell>();

			falling = new PassiveFalling(); 
			breath = new PassiveBreath();   
			burn = new PassiveBurn();       
			defend = new PassiveDefend();  
			
			passives.put(falling.getID(), falling);
			passives.put(breath.getID(), breath);
			passives.put(burn.getID(), burn);
			passives.put(defend.getID(), defend);
		}

		public static IPassiveSpell falling;
		public static IPassiveSpell breath;
		public static IPassiveSpell burn;
		public static IPassiveSpell defend;
		
		public static IPassiveSpell getByID(int id){
			return passives.get(id);
		}
	}

	public static void register(){

		screen = new SpellScreenRender();
		caster = new SpellCaster();
		spellbook = new ArrayList<ISpell>();
		hashbook = new HashMap<Integer, ISpell>();
		
		Passives.register();
		
		int spellId = -1;// the smallest spell gets id zero

		Spells.ghost = new SpellGhost(++spellId, "ghost");
		registerSpell(Spells.ghost);

		Spells.phase = new SpellPhasing(++spellId, "phasing");
		registerSpell(Spells.phase);

		Spells.waterwalk = new SpellPotion(++spellId, "waterwalk", 45);
		Spells.waterwalk.setPotion(PotionRegistry.waterwalk.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(Spells.waterwalk);

		Spells.nightvision = new SpellPotion(++spellId, "nightvision", 30);
		Spells.nightvision.setPotion(Potion.nightVision.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(Spells.nightvision);

		Spells.haste = new SpellPotion(++spellId, "haste", 50);
		Spells.haste.setPotion(Potion.digSpeed.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(Spells.haste);

		Spells.replacer = new SpellRangeReplace(++spellId, "replacer");
		registerSpell(Spells.replacer);

		Spells.rotate = new SpellRangeRotate(++spellId, "rotate");
		registerSpell(Spells.rotate);

		Spells.reach = new SpellRangeBuild(++spellId, "reach");
		registerSpell(Spells.reach);

		Spells.inventory = new SpellInventory(++spellId, "inventory");
		registerSpell(Spells.inventory);

		Spells.push = new SpellRangePush(++spellId, "push");
		registerSpell(Spells.push);

		Spells.pull = new SpellRangePull(++spellId, "pull");
		registerSpell(Spells.pull);

		Spells.chestsack = new SpellChestSack(++spellId, "chestsack");
		registerSpell(Spells.chestsack);

		Spells.torch = new SpellThrowTorch(++spellId, "torch");
		registerSpell(Spells.torch);

		Spells.fishing = new SpellThrowFishing(++spellId, "fishing");
		registerSpell(Spells.fishing);

		Spells.ice = new SpellThrowIce(++spellId, "ice");
		registerSpell(Spells.ice);

		Spells.shear = new SpellThrowShear(++spellId, "shear");
		registerSpell(Spells.shear);

		SpellThrowHarvest harvest = new SpellThrowHarvest(++spellId, "harvest");
		registerSpell(harvest);

		SpellThrowWater water = new SpellThrowWater(++spellId, "water");
		registerSpell(water);

		Spells.lightning = new SpellThrowLightning(++spellId, "lightning");
		registerSpell(Spells.lightning);

		Spells.spawnegg = new SpellThrowSpawnEgg(++spellId, "spawnegg");
		registerSpell(Spells.spawnegg);

		Spells.scaffold = new SpellScaffolding(++spellId, "scaffold");
		registerSpell(Spells.scaffold);

		Spells.carbon = new SpellCarbonPaper(++spellId, "carbon");
		registerSpell(Spells.carbon);

		Spells.launch = new SpellLaunch(++spellId, "launch");
		registerSpell(Spells.launch);

		Spells.waypoint = new SpellLinkingPortal(++spellId, "waypoint");
		registerSpell(Spells.waypoint);

		Spells.magnet = new SpellPotion(++spellId, "magnet", 50);
		Spells.magnet.setPotion(PotionRegistry.magnet.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(Spells.magnet);

	}

	private static void registerSpell(ISpell spell){

		spellbook.add(spell);
		hashbook.put(spell.getID(), spell);
	}

	public static ISpell getDefaultSpell(){

		return getSpellFromID(0);
	}

	public static boolean spellsEnabled(EntityPlayer player){

		ItemStack held = player.getHeldItem();
		return held != null && held.getItem() instanceof ItemCyclicWand;
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
