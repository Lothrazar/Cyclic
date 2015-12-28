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
		return held != null && held.getItem() == ItemRegistry.master_wand;
	}
	 
	public static void register() {
		screen = new SpellScreenRender();
		caster = new SpellCaster();
		spellbook = new ArrayList<ISpell>();
		//Map<String,Integer> idMap = new HashMap<String,Integer>();

		int potionDuration = Const.TICKS_PER_SEC * 20;

		// used to be public statics
		BaseSpell ghost;
		BaseSpell phase;
		SpellExpPotion waterwalk;
		SpellExpPotion nightvision;//TODO: replace with night vision
		BaseSpell rotate;
		BaseSpell push;
		SpellThrowTorch torch;
		SpellThrowFishing fishing;
		BaseSpell carbon;

		ghost = new SpellGhost(0,"ghost");
		registerSpell(ghost);
		
		phase = new SpellPhasing(1,"phasing");
		registerSpell(phase);

		waterwalk = new SpellExpPotion(2,"waterwalk");
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I);
		registerSpell(waterwalk);

		nightvision = new SpellExpPotion(3,"nightvision");
		nightvision.setPotion(Potion.nightVision.id, potionDuration, PotionRegistry.I);
		registerSpell(nightvision);

		rotate = new SpellRotate(4,"rotate"); 
		registerSpell(rotate);

		push = new SpellPush(5,"push");
		registerSpell(push);

		SpellPull pull = new SpellPull(6,"pull");
		registerSpell(pull);

		torch = new SpellThrowTorch(7,"torch");
		registerSpell(torch);

		fishing = new SpellThrowFishing(8,"fishing");
		registerSpell(fishing);

		SpellThrowExplosion explode = new SpellThrowExplosion(9,"explode");
		registerSpell(explode);

		SpellThrowFire fire = new SpellThrowFire(10,"fire");
		registerSpell(fire);

		SpellThrowIce ice = new SpellThrowIce(11,"ice");
		registerSpell(ice);

		SpellThrowLightning lightning = new SpellThrowLightning(12,"lightning");
		registerSpell(lightning);

		SpellThrowShear shear = new SpellThrowShear(13,"shear");
		registerSpell(shear);

		SpellThrowWater water = new SpellThrowWater(14,"water");
		registerSpell(water);

		SpellScaffolding scaffold = new SpellScaffolding(15,"scaffold");
		registerSpell(scaffold);

		SpellChestSack chestsack = new SpellChestSack(16,"chestsack");
		registerSpell(chestsack);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(17,"spawnegg");
		registerSpell(spawnegg);
		
		carbon = new SpellCarbonPaper(18,"carbon");
		registerSpell(carbon);
		
		SpellLaunch launch = new SpellLaunch(19,"launch");
		registerSpell(launch);
		
		SpellThrowHarvest harvest = new SpellThrowHarvest(20,"harvest");
		registerSpell(harvest);
		
		SpellLinkingPortal waypoint = new SpellLinkingPortal(21,"waypoint");
		registerSpell(waypoint);
		
		SpellBuilder builder = new SpellBuilder(22,"builder");
		registerSpell(builder);
		
		SpellReplacer replacer = new SpellReplacer(23,"replacer");
		registerSpell(replacer);
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
