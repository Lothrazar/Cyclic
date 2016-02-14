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

		SpellGhost ghost = new SpellGhost(++spellId, "ghost");
		registerSpell(ghost);

		SpellPhasing phase = new SpellPhasing(++spellId, "phasing");
		registerSpell(phase);

		SpellPotion waterwalk = new SpellPotion(++spellId, "waterwalk", 45);
		waterwalk.setPotion(PotionRegistry.waterwalk.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(waterwalk);

		SpellPotion nightvision = new SpellPotion(++spellId, "nightvision", 30);
		nightvision.setPotion(Potion.nightVision.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(nightvision);

		SpellPotion haste = new SpellPotion(++spellId, "haste", 50);
		haste.setPotion(Potion.digSpeed.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(haste);

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

		SpellChestSack chestsack = new SpellChestSack(++spellId, "chestsack");
		registerSpell(chestsack);

		SpellThrowTorch torch = new SpellThrowTorch(++spellId, "torch");
		registerSpell(torch);

		SpellThrowFishing fishing = new SpellThrowFishing(++spellId, "fishing");
		registerSpell(fishing);

		SpellThrowIce ice = new SpellThrowIce(++spellId, "ice");
		registerSpell(ice);

		SpellThrowShear shear = new SpellThrowShear(++spellId, "shear");
		registerSpell(shear);

		SpellThrowHarvest harvest = new SpellThrowHarvest(++spellId, "harvest");
		registerSpell(harvest);

		SpellThrowWater water = new SpellThrowWater(++spellId, "water");
		registerSpell(water);

		SpellThrowLightning lightning = new SpellThrowLightning(++spellId, "lightning");
		registerSpell(lightning);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId, "spawnegg");
		registerSpell(spawnegg);

		SpellScaffolding scaffold = new SpellScaffolding(++spellId, "scaffold");
		registerSpell(scaffold);

		SpellCarbonPaper carbon = new SpellCarbonPaper(++spellId, "carbon");
		registerSpell(carbon);

		SpellLaunch launch = new SpellLaunch(++spellId, "launch");
		registerSpell(launch);

		SpellLinkingPortal waypoint = new SpellLinkingPortal(++spellId, "waypoint");
		registerSpell(waypoint);

		SpellPotion magnet = new SpellPotion(++spellId, "magnet", 50);
		magnet.setPotion(PotionRegistry.magnet.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(magnet);

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
