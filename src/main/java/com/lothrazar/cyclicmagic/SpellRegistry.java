package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

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

		ghost = new SpellGhost(++spellId);
		ghost.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/ghost.png"));
		spellbook.add(ghost);

		jump = new SpellExpPotion(++spellId);
		jump.setPotion(Potion.jump.id, potionDuration, PotionRegistry.V).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/jump.png"));
		spellbook.add(jump);

		phase = new SpellPhasing(++spellId);
		phase.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/phasing.png"));
		spellbook.add(phase);

		slowfall = new SpellExpPotion(++spellId);
		slowfall.setPotion(PotionRegistry.slowfall.id, potionDuration, PotionRegistry.I);
		slowfall.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/slowfall.png"));
		spellbook.add(slowfall);

		waterwalk = new SpellExpPotion(++spellId);
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/waterwalk.png"));
		spellbook.add(waterwalk);

		haste = new SpellExpPotion(++spellId);
		haste.setPotion(Potion.digSpeed.id, potionDuration, PotionRegistry.II).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/haste.png"));
		spellbook.add(haste);

		collect = new SpellCollect(++spellId);
		collect.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/collect.png"));
		spellbook.add(collect);

		rotate = new SpellRotate(++spellId);
		rotate.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/rotate.png"));
		spellbook.add(rotate);

		push = new SpellPush(++spellId);
		push.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/push.png"));
		spellbook.add(push);

		SpellPull pull = new SpellPull(++spellId);
		pull.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/pull.png"));
		spellbook.add(pull);

		torch = new SpellThrowTorch(++spellId);
		torch.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/torch.png"));
		spellbook.add(torch);

		fishing = new SpellThrowFishing(++spellId);
		fishing.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/fishing.png"));
		spellbook.add(fishing);

		SpellThrowExplosion explode = new SpellThrowExplosion(++spellId);
		explode.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/explode.png"));
		spellbook.add(explode);

		SpellThrowFire fire = new SpellThrowFire(++spellId);
		fire.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/fire.png"));
		spellbook.add(fire);

		SpellThrowIce ice = new SpellThrowIce(++spellId);
		ice.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/ice.png"));
		spellbook.add(ice);

		SpellThrowLightning lightning = new SpellThrowLightning(++spellId);
		lightning.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/lightning.png"));
		spellbook.add(lightning);

		SpellThrowShear shear = new SpellThrowShear(++spellId);
		shear.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/shear.png"));
		spellbook.add(shear);

		SpellThrowWater water = new SpellThrowWater(++spellId);
		water.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/water.png"));
		spellbook.add(water);

		SpellScaffolding scaffold = new SpellScaffolding(++spellId);
		scaffold.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/scaffold.png"));
		spellbook.add(scaffold);

		SpellChestSack chestsack = new SpellChestSack(++spellId);
		chestsack.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/chestsack.png"));
		spellbook.add(chestsack);

		SpellThrowSpawnEgg spawnegg = new SpellThrowSpawnEgg(++spellId);
		spawnegg.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/spawnegg.png"));
		spellbook.add(spawnegg);
		
		carbon = new SpellCarbonPaper(++spellId);
		carbon.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/carbon.png"));
		spellbook.add(carbon);
		
		SpellGiveLauncher launch = new SpellGiveLauncher(++spellId);
		launch.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/launch.png"));
		spellbook.add(launch);
	}

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
}
