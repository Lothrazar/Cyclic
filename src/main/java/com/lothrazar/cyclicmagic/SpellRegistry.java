package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class SpellRegistry {

	private static ArrayList<ISpell> spellbook;
	
	static SpellScreenRender screen;
	public static SpellCaster caster;

	private static void registerSpell(ISpell spell){
		spellbook.add(spell);
	}
	
	public static ISpell getDefaultSpell() {
		return SpellRegistry.getSpellbook().get(0);
	}

	public static boolean spellsEnabled(EntityPlayer player) {
		ItemStack held = player.getHeldItem();
		return held != null && held.getItem() == ItemRegistry.cyclic_wand;
	}
	private static ArrayList<Integer> typeBuilding;
	private static ArrayList<Integer> typeExploration;
	private static ArrayList<Integer> typeDestruction;
	private static ArrayList<Integer> typeFarming;
	
	public static final int TYPE_BUILDING = 1;
	public static final int TYPE_EXPLORATION = 2;
	public static final int TYPE_DESTRUCTION = 3;
	public static final int TYPE_FARMING = 4;
	public static ArrayList<Integer> getByType(int type){
		switch(type){
		case TYPE_BUILDING: return typeBuilding;
		case TYPE_EXPLORATION: return typeExploration;
		case TYPE_DESTRUCTION: return typeDestruction;
		case TYPE_FARMING: return typeFarming;
		}
		return new ArrayList<Integer>();
	}
	
	public static void register() {
		screen = new SpellScreenRender();
		caster = new SpellCaster();
		spellbook = new ArrayList<ISpell>();
		typeBuilding = new ArrayList<Integer>();
		typeExploration = new ArrayList<Integer>();
		typeDestruction = new ArrayList<Integer>(); 
		typeFarming = new ArrayList<Integer>(); 

		int potionDuration = Const.TICKS_PER_SEC * 20;

		int spellId = -1;//the smallest spell gets id zero

		SpellGhost ghost = new SpellGhost(++spellId,"ghost");
		registerSpell(ghost);
		typeExploration.add(ghost.getID());
		
		SpellPhasing phase = new SpellPhasing(++spellId,"phasing");
		registerSpell(phase);
		typeExploration.add(phase.getID());

		SpellExpPotion waterwalk = new SpellExpPotion(++spellId,"waterwalk");
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I);
		registerSpell(waterwalk);
		typeExploration.add(waterwalk.getID());

		SpellExpPotion nightvision = new SpellExpPotion(++spellId,"nightvision");
		nightvision.setPotion(Potion.nightVision.id, potionDuration, PotionRegistry.I);
		registerSpell(nightvision);
		typeExploration.add(nightvision.getID());
		
		SpellBuilder builder = new SpellBuilder(++spellId,"builder");
		registerSpell(builder);
		typeBuilding.add(builder.getID());
		
		SpellReplacer replacer = new SpellReplacer(++spellId,"replacer");
		registerSpell(replacer);
		typeBuilding.add(replacer.getID());

		SpellRotate rotate = new SpellRotate(++spellId,"rotate"); 
		registerSpell(rotate);
		typeBuilding.add(rotate.getID());

		SpellPush push = new SpellPush(++spellId,"push");
		registerSpell(push);
		typeBuilding.add(push.getID());

		SpellPull pull = new SpellPull(++spellId,"pull");
		registerSpell(pull);
		typeBuilding.add(pull.getID());

		SpellChestSack chestsack = new SpellChestSack(++spellId,"chestsack");
		registerSpell(chestsack);
		typeBuilding.add(pull.getID());

		SpellThrowTorch torch = new SpellThrowTorch(++spellId,"torch");
		registerSpell(torch);
		typeExploration.add(torch.getID());

		SpellThrowFishing fishing = new SpellThrowFishing(++spellId,"fishing");
		registerSpell(fishing);
		typeFarming.add(fishing.getID());

		SpellThrowExplosion explode = new SpellThrowExplosion(++spellId,"explode");
		registerSpell(explode);
		typeDestruction.add(explode.getID());

		SpellThrowFire fire = new SpellThrowFire(++spellId,"fire");
		registerSpell(fire);
		typeDestruction.add(fire.getID());

		SpellThrowIce ice = new SpellThrowIce(++spellId,"ice");
		registerSpell(ice);

		SpellThrowLightning lightning = new SpellThrowLightning(++spellId,"lightning");
		registerSpell(lightning);
		typeDestruction.add(lightning.getID());

		SpellThrowShear shear = new SpellThrowShear(++spellId,"shear");
		registerSpell(shear);
		typeFarming.add(shear.getID());
		
		SpellThrowHarvest harvest = new SpellThrowHarvest(++spellId,"harvest");
		registerSpell(harvest);
		typeFarming.add(harvest.getID());
		
		SpellThrowWater water = new SpellThrowWater(++spellId,"water");
		registerSpell(water);
		typeFarming.add(water.getID());

		SpellScaffolding scaffold = new SpellScaffolding(++spellId,"scaffold");
		registerSpell(scaffold);
		typeBuilding.add(scaffold.getID());

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId,"spawnegg");
		registerSpell(spawnegg);
		typeFarming.add(spawnegg.getID());
		
		SpellCarbonPaper carbon = new SpellCarbonPaper(++spellId,"carbon");
		registerSpell(carbon);
		typeBuilding.add(carbon.getID());
		
		SpellLaunch launch = new SpellLaunch(++spellId,"launch");
		registerSpell(launch);
		typeExploration.add(spawnegg.getID());
		typeBuilding.add(spawnegg.getID());
		
		SpellLinkingPortal waypoint = new SpellLinkingPortal(++spellId,"waypoint");
		registerSpell(waypoint);
		typeExploration.add(waypoint.getID());
	}

	public static ISpell getSpellFromID(int id) {
		
		if(id >= spellbook.size()){
			return null;//this should avoid all OOB exceptoins
		}
		
		try{
			return spellbook.get(id);
		}
		catch(IndexOutOfBoundsException  e){
			return null;
		}
	}

	public static ArrayList<ISpell> getSpellbook() {
		return spellbook;
	}
}
