package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class SpellRegistry {

	public static ArrayList<ISpell> spellbook;

	public static boolean spellsEnabled(EntityPlayer player){
		ItemStack held = player.getHeldItem();
		return held != null && held.getItem() == ItemRegistry.master_wand;
	}

	public static void register() {
		spellbook = new ArrayList<ISpell>();

		int potionDuration = Const.TICKS_PER_SEC * 20;

		int spellId = 0;

		// used to be public statics
		BaseSpellExp ghost;
		SpellExpPotion jump;
		BaseSpellExp phase;
		SpellExpPotion slowfall;
		SpellExpPotion waterwalk;
		SpellExpPotion haste;
		SpellCollect collect;
		BaseSpellExp rotate;
		BaseSpellExp push;
		SpellThrowTorch torch;
		SpellThrowFishing fishing;

		ghost = new SpellGhost();
		//ghost.setExpCost(ModMain.cfg.ghost)
		ghost.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/ghost.png")).setSpellID(++spellId);
		spellbook.add(ghost);

		jump = new SpellExpPotion();
		jump.setPotion(Potion.jump.id, potionDuration, PotionRegistry.V).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/jump.png")).setSpellID(++spellId);
		spellbook.add(jump);

		phase = new SpellPhasing();
		//phase.setExpCost(ModMain.cfg.phase)
		phase.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/phasing.png")).setSpellID(++spellId);
		spellbook.add(phase);

		slowfall = new SpellExpPotion();
		slowfall.setPotion(PotionRegistry.slowfall.id, potionDuration, PotionRegistry.I);
		//slowfall.setExpCost(ModMain.cfg.slowfall);
		slowfall.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/slowfall.png"));
		slowfall.setSpellID(++spellId);
		spellbook.add(slowfall);

		waterwalk = new SpellExpPotion();
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/waterwalk.png")).setSpellID(++spellId);
		spellbook.add(waterwalk);

		haste = new SpellExpPotion();
		haste.setPotion(Potion.digSpeed.id, potionDuration, PotionRegistry.II).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/haste.png")).setSpellID(++spellId);
		spellbook.add(haste);

		collect = new SpellCollect();
		collect.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/collect.png")).setSpellID(++spellId);
		spellbook.add(collect);

		rotate = new SpellRotate();
		rotate.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/rotate.png")).setSpellID(++spellId);
		spellbook.add(rotate);

		push = new SpellPush();
		push.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/push.png")).setSpellID(++spellId);
		spellbook.add(push);

		SpellPull pull = new SpellPull();
		pull.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/pull.png")).setSpellID(++spellId);
		spellbook.add(pull);

		torch = new SpellThrowTorch();
		torch.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/torch.png")).setSpellID(++spellId);
		spellbook.add(torch);

		fishing = new SpellThrowFishing();
		fishing.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/fishing.png")).setSpellID(++spellId);
		spellbook.add(fishing);

		SpellThrowExplosion explode = new SpellThrowExplosion();
		explode.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/explode.png")).setSpellID(++spellId);
		spellbook.add(explode);

		SpellThrowFire fire = new SpellThrowFire();
		fire.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/fire.png")).setSpellID(++spellId);
		spellbook.add(fire);

		SpellThrowIce ice = new SpellThrowIce();
		ice.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/ice.png")).setSpellID(++spellId);
		spellbook.add(ice);

		SpellThrowLightning lightning = new SpellThrowLightning();
		lightning.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/lightning.png")).setSpellID(++spellId);
		spellbook.add(lightning);

		SpellThrowShear shear = new SpellThrowShear();
		shear.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/shear.png")).setSpellID(++spellId);
		spellbook.add(shear);

		SpellThrowWater water = new SpellThrowWater();
		water.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/water.png")).setSpellID(++spellId);
		spellbook.add(water);

		SpellScaffolding scaffold = new SpellScaffolding();
		scaffold.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/scaffold.png")).setSpellID(++spellId);
		spellbook.add(scaffold);
		
		SpellChestSack chestsack = new SpellChestSack();
		chestsack.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/chestsack.png")).setSpellID(++spellId);
		spellbook.add(chestsack);
		
		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg();
		spawnegg.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/spawnegg.png")).setSpellID(++spellId);
		spellbook.add(spawnegg);
	}

	public static ISpell getSpellFromID(int id) {
		if (id == 0) {
			return null;
		}
		for (ISpell sp : SpellRegistry.spellbook) {
			if (sp.getSpellID() == id) {
				return sp;
			}
		}

		return null;
	}
}
