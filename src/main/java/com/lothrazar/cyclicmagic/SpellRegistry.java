package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
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
		return held != null && held.getItem() instanceof ItemCyclicWand;
	}

	public static void register() {
		screen = new SpellScreenRender();
		caster = new SpellCaster();
		spellbook = new ArrayList<ISpell>();
  
		int spellId = -1;//the smallest spell gets id zero

		SpellGhost ghost = new SpellGhost(++spellId,"ghost");
		registerSpell(ghost);
		
		SpellPhasing phase = new SpellPhasing(++spellId,"phasing");
		registerSpell(phase);

		SpellExpPotion waterwalk = new SpellExpPotion(++spellId,"waterwalk",45);
		waterwalk.setPotion(PotionRegistry.waterwalk.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(waterwalk);

		SpellExpPotion nightvision = new SpellExpPotion(++spellId,"nightvision",30);
		nightvision.setPotion(Potion.nightVision.id, Const.TICKS_PER_SEC * 30, PotionRegistry.I);
		registerSpell(nightvision);

		SpellExpPotion haste = new SpellExpPotion(++spellId,"haste",50);
		haste.setPotion(Potion.digSpeed.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(haste);
		
		SpellBuilder builder = new SpellBuilder(++spellId,"builder");
		registerSpell(builder);
		
		SpellReplacer replacer = new SpellReplacer(++spellId,"replacer");
		registerSpell(replacer);

		SpellFarReach reach = new SpellFarReach(++spellId,"reach");
		registerSpell(reach);
		
		SpellRotate rotate = new SpellRotate(++spellId,"rotate"); 
		registerSpell(rotate);

		SpellPush push = new SpellPush(++spellId,"push");
		registerSpell(push);

		SpellPull pull = new SpellPull(++spellId,"pull");
		registerSpell(pull);

		SpellChestSack chestsack = new SpellChestSack(++spellId,"chestsack");
		registerSpell(chestsack);

		SpellThrowTorch torch = new SpellThrowTorch(++spellId,"torch");
		registerSpell(torch);

		SpellThrowFishing fishing = new SpellThrowFishing(++spellId,"fishing");
		registerSpell(fishing);

		SpellThrowIce ice = new SpellThrowIce(++spellId,"ice");
		registerSpell(ice);

		SpellThrowShear shear = new SpellThrowShear(++spellId,"shear");
		registerSpell(shear);
		
		SpellThrowHarvest harvest = new SpellThrowHarvest(++spellId,"harvest");
		registerSpell(harvest);
		
		SpellThrowWater water = new SpellThrowWater(++spellId,"water");
		registerSpell(water);

		SpellScaffolding scaffold = new SpellScaffolding(++spellId,"scaffold");
		registerSpell(scaffold);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId,"spawnegg");
		registerSpell(spawnegg);
		
		SpellCarbonPaper carbon = new SpellCarbonPaper(++spellId,"carbon");
		registerSpell(carbon);
		
		SpellLaunch launch = new SpellLaunch(++spellId,"launch");
		registerSpell(launch);
		
		SpellLinkingPortal waypoint = new SpellLinkingPortal(++spellId,"waypoint");
		registerSpell(waypoint);
		 
		SpellExpPotion magnet = new SpellExpPotion(++spellId,"magnet",50);
		magnet.setPotion(PotionRegistry.magnet.id, Const.TICKS_PER_SEC * 60, PotionRegistry.II);
		registerSpell(magnet);
		
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
