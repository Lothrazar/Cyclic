package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	public static List<Integer> builder = new ArrayList<Integer>(); 
	public static List<Integer> explorer = new ArrayList<Integer>();
	public static List<Integer> farmer = new ArrayList<Integer>();

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
		public static SpellThrowHarvest harvest;
		public static SpellThrowWater water;
		public static SpellRangeBuild reachup;
		public static SpellRangeBuild reachplace;
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

		Spells.reachdown = new SpellRangeBuild(++spellId, "reachdown",SpellRangeBuild.PlaceType.DOWN);
		registerSpell(Spells.reachdown);

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

		Spells.harvest = new SpellThrowHarvest(++spellId, "harvest");
		registerSpell(Spells.harvest);

		Spells.water = new SpellThrowWater(++spellId, "water");
		registerSpell(Spells.water);

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

		Spells.reachup = new SpellRangeBuild(++spellId, "reachup",SpellRangeBuild.PlaceType.UP);
		registerSpell(Spells.reachup);
		
		Spells.reachplace = new SpellRangeBuild(++spellId, "reachplace",SpellRangeBuild.PlaceType.PLACE);
		registerSpell(Spells.reachplace);
		
		Collections.addAll(builder, SpellRegistry.Spells.inventory.getID()
				, SpellRegistry.Spells.pull.getID()
				, SpellRegistry.Spells.push.getID()
				, SpellRegistry.Spells.scaffold.getID()
				,SpellRegistry.Spells.launch.getID()
				, SpellRegistry.Spells.rotate.getID()
				, SpellRegistry.Spells.replacer.getID()
				, SpellRegistry.Spells.reachdown.getID()
				, SpellRegistry.Spells.reachup.getID()
				, SpellRegistry.Spells.reachplace.getID() 
				,SpellRegistry.Spells.haste.getID()
				);
 
		Collections.addAll(explorer, SpellRegistry.Spells.inventory.getID()
				,SpellRegistry.Spells.nightvision.getID()
				,SpellRegistry.Spells.ghost.getID()
				,SpellRegistry.Spells.launch.getID()
				,SpellRegistry.Spells.torch.getID()
				,SpellRegistry.Spells.waterwalk.getID()
				,SpellRegistry.Spells.waypoint.getID()
				,SpellRegistry.Spells.phase.getID()
				,SpellRegistry.Spells.spawnegg.getID()
				,SpellRegistry.Spells.haste.getID()
				);


		Collections.addAll(farmer,SpellRegistry.Spells.inventory.getID()
				,SpellRegistry.Spells.shear.getID()
				,SpellRegistry.Spells.magnet.getID()
				,SpellRegistry.Spells.harvest.getID()
				,SpellRegistry.Spells.water.getID()
				,SpellRegistry.Spells.chestsack.getID()
				,SpellRegistry.Spells.fishing.getID()
				,SpellRegistry.Spells.launch.getID()
				,SpellRegistry.Spells.haste.getID()
				);
		
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
