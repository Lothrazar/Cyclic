package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class SpellRegistry {

	private static ArrayList<ISpell> spellbook;

	private static void registerSpell(ISpell spell){
		spellbook.add(spell);
		
		//System.out.println("spell "+spell.getID()+ " registered "+spell.getName());
	}
	public static ISpell getDefaultSpell() {
		return SpellRegistry.getSpellbook().get(0);
	}

	public static boolean spellsEnabled(EntityPlayer player) {
		ItemStack held = player.getHeldItem();
		return held != null && held.getItem() == ItemRegistry.master_wand;
	}
	 
	public static void register() {
		spellbook = new ArrayList<ISpell>();

		int potionDuration = Const.TICKS_PER_SEC * 20;

		int spellId = -1;//the smallest spell gets id zero

		// used to be public statics
		BaseSpell ghost;
		SpellExpPotion jump;
		BaseSpell phase;
		SpellExpPotion slowfall;
		SpellExpPotion waterwalk;
		SpellExpPotion haste;
		BaseSpell rotate;
		BaseSpell push;
		SpellThrowTorch torch;
		SpellThrowFishing fishing;
		BaseSpell carbon;

		ghost = new SpellGhost(++spellId,"ghost");
		registerSpell(ghost);
		
		jump = new SpellExpPotion(++spellId,"jump");
		jump.setPotion(Potion.jump.id, potionDuration, PotionRegistry.V);
		registerSpell(jump);

		phase = new SpellPhasing(++spellId,"phasing");
		registerSpell(phase);

		slowfall = new SpellExpPotion(++spellId,"slowfall");
		slowfall.setPotion(PotionRegistry.slowfall.id, potionDuration, PotionRegistry.I);
		registerSpell(slowfall);

		waterwalk = new SpellExpPotion(++spellId,"waterwalk");
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I);
		registerSpell(waterwalk);

		haste = new SpellExpPotion(++spellId,"haste");
		haste.setPotion(Potion.digSpeed.id, potionDuration, PotionRegistry.II);
		registerSpell(haste);

		rotate = new SpellRotate(++spellId,"rotate"); 
		registerSpell(rotate);

		push = new SpellPush(++spellId,"push");
		registerSpell(push);

		SpellPull pull = new SpellPull(++spellId,"pull");
		registerSpell(pull);

		torch = new SpellThrowTorch(++spellId,"torch");
		registerSpell(torch);

		fishing = new SpellThrowFishing(++spellId,"fishing");
		registerSpell(fishing);

		SpellThrowExplosion explode = new SpellThrowExplosion(++spellId,"explode");
		registerSpell(explode);

		SpellThrowFire fire = new SpellThrowFire(++spellId,"fire");
		registerSpell(fire);

		SpellThrowIce ice = new SpellThrowIce(++spellId,"ice");
		registerSpell(ice);

		SpellThrowLightning lightning = new SpellThrowLightning(++spellId,"lightning");
		registerSpell(lightning);

		SpellThrowShear shear = new SpellThrowShear(++spellId,"shear");
		registerSpell(shear);

		SpellThrowWater water = new SpellThrowWater(++spellId,"water");
		registerSpell(water);

		SpellScaffolding scaffold = new SpellScaffolding(++spellId,"scaffold");
		registerSpell(scaffold);

		SpellChestSack chestsack = new SpellChestSack(++spellId,"chestsack");
		registerSpell(chestsack);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId,"spawnegg");
		registerSpell(spawnegg);
		
		carbon = new SpellCarbonPaper(++spellId,"carbon");
		registerSpell(carbon);
		
		SpellLaunch launch = new SpellLaunch(++spellId,"launch");
		registerSpell(launch);
		
		SpellThrowHarvest harvest = new SpellThrowHarvest(++spellId,"harvest");
		registerSpell(harvest);
		
		SpellLinkingPortal waypoint = new SpellLinkingPortal(++spellId,"waypoint");
		registerSpell(waypoint);
	}

	public static ISpell getSpellFromID(int id) {
		
		if(id >= spellbook.size()){
			return null;//this should avoid all OOB exceptoins
		}
		
		try{
			return spellbook.get(id);
		}
		catch(IndexOutOfBoundsException  e){
			System.out.println(id+" SPELL OOB fix yo stuff k");
			return null;
		}
		 /*
		for (ISpell sp : SpellRegistry.getSpellbook()) {
			if (sp.getID() == id) {
				return sp;
			}
		}

		return null;
		*/
	}
	/*
	public static ISpell left(ISpell self) { 
		int idx = SpellRegistry.spellbook.indexOf(self);// -1 for not found
		if (idx == -1) {
			return null;
		}

		if (idx == 0)
			idx = SpellRegistry.spellbook.size() - 1;
		else
			idx = idx - 1;

		return SpellRegistry.spellbook.get(idx);
	}

	public static ISpell right(ISpell self) { 
		int idx = SpellRegistry.spellbook.indexOf(self);
		if (idx == -1) {
			return null;
		}

		if (idx == SpellRegistry.spellbook.size() - 1)
			idx = 0;
		else
			idx = idx + 1;

		return SpellRegistry.spellbook.get(idx);
	}
	*/

	public static ArrayList<ISpell> getSpellbook() {
		return spellbook;
	}
}
