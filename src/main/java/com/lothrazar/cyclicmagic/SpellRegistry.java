package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class SpellRegistry {

	public static ArrayList<ISpell> spellbook;

	public static boolean spellsEnabled(EntityPlayer player) {
		ItemStack held = player.getHeldItem();
		return held != null && held.getItem() == ItemRegistry.master_wand;
	}

	public static void register() {
		spellbook = new ArrayList<ISpell>();

		int potionDuration = Const.TICKS_PER_SEC * 20;

		int spellId = 0;

		// used to be public statics
		BaseSpell ghost;
		SpellExpPotion jump;
		BaseSpell phase;
		SpellExpPotion slowfall;
		SpellExpPotion waterwalk;
		SpellExpPotion haste;
		SpellCollect collect;
		BaseSpell rotate;
		BaseSpell push;
		SpellThrowTorch torch;
		SpellThrowFishing fishing;
		BaseSpell carbon;

		ghost = new SpellGhost(++spellId,"ghost");
		spellbook.add(ghost);

		jump = new SpellExpPotion(++spellId,"jump");
		jump.setPotion(Potion.jump.id, potionDuration, PotionRegistry.V);
		spellbook.add(jump);

		phase = new SpellPhasing(++spellId,"phasing");
		spellbook.add(phase);

		slowfall = new SpellExpPotion(++spellId,"slowfall");
		slowfall.setPotion(PotionRegistry.slowfall.id, potionDuration, PotionRegistry.I);
		spellbook.add(slowfall);

		waterwalk = new SpellExpPotion(++spellId,"waterwalk");
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I);
		spellbook.add(waterwalk);

		haste = new SpellExpPotion(++spellId,"haste");
		haste.setPotion(Potion.digSpeed.id, potionDuration, PotionRegistry.II);
		spellbook.add(haste);

		collect = new SpellCollect(++spellId,"collect");
		spellbook.add(collect);

		rotate = new SpellRotate(++spellId,"rotate"); 
		spellbook.add(rotate);

		push = new SpellPush(++spellId,"push");
		spellbook.add(push);

		SpellPull pull = new SpellPull(++spellId,"pull");
		spellbook.add(pull);

		torch = new SpellThrowTorch(++spellId,"torch");
		spellbook.add(torch);

		fishing = new SpellThrowFishing(++spellId,"fishing");
		spellbook.add(fishing);

		SpellThrowExplosion explode = new SpellThrowExplosion(++spellId,"explode");
		spellbook.add(explode);

		SpellThrowFire fire = new SpellThrowFire(++spellId,"fire");
		spellbook.add(fire);

		SpellThrowIce ice = new SpellThrowIce(++spellId,"ice");
		spellbook.add(ice);

		SpellThrowLightning lightning = new SpellThrowLightning(++spellId,"lightning");
		spellbook.add(lightning);

		SpellThrowShear shear = new SpellThrowShear(++spellId,"shear");
		spellbook.add(shear);

		SpellThrowWater water = new SpellThrowWater(++spellId,"water");
		spellbook.add(water);

		SpellScaffolding scaffold = new SpellScaffolding(++spellId,"scaffold");
		spellbook.add(scaffold);

		SpellChestSack chestsack = new SpellChestSack(++spellId,"chestsack");
		spellbook.add(chestsack);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId,"spawnegg");
		spellbook.add(spawnegg);
		
		carbon = new SpellCarbonPaper(++spellId,"carbon");
		spellbook.add(carbon);
		
		SpellGiveLauncher launch = new SpellGiveLauncher(++spellId,"launch");
		spellbook.add(launch);
		
		largestSpellId = spellId;
	}

	public static int largestSpellId;//workaround to stop powerups crashing
	
	public static ISpell getSpellFromID(int id) {
		if (id == 0) {
			return null;
		}
		for (ISpell sp : SpellRegistry.spellbook) {
			if (sp.getID() == id) {
				return sp;
			}
		}

		return null;
	}
	
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
	
}
