package com.lothrazar.cyclicmagic;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import com.lothrazar.cyclicmagic.spell.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellRegistry {

	public static ArrayList<ISpell> spellbook;
	public static final int SPELL_TOGGLE_HIDE = 0;
	public static final int SPELL_TOGGLE_SHOW = 1;

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
		ghost.setExpCost(ModMain.cfg.ghost).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/ghost.png")).setSpellID(++spellId);
		spellbook.add(ghost);

		jump = new SpellExpPotion();
		jump.setPotion(Potion.jump.id, potionDuration, PotionRegistry.V).setExpCost(ModMain.cfg.jump).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/jump.png")).setSpellID(++spellId);
		spellbook.add(jump);

		phase = new SpellPhasing();
		phase.setExpCost(ModMain.cfg.phase).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/phasing.png")).setSpellID(++spellId);
		spellbook.add(phase);

		slowfall = new SpellExpPotion();
		slowfall.setPotion(PotionRegistry.slowfall.id, potionDuration, PotionRegistry.I);
		slowfall.setExpCost(ModMain.cfg.slowfall);
		slowfall.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/slowfall.png"));
		slowfall.setSpellID(++spellId);
		spellbook.add(slowfall);

		waterwalk = new SpellExpPotion();
		waterwalk.setPotion(PotionRegistry.waterwalk.id, potionDuration, PotionRegistry.I).setExpCost(ModMain.cfg.waterwalk).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/waterwalk.png")).setSpellID(++spellId);
		spellbook.add(waterwalk);

		haste = new SpellExpPotion();
		haste.setPotion(Potion.digSpeed.id, potionDuration, PotionRegistry.II).setExpCost(ModMain.cfg.haste).setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/haste.png")).setSpellID(++spellId);
		spellbook.add(haste);

		collect = new SpellCollect();
		collect.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/collect.png")).setSpellID(++spellId);
		spellbook.add(collect);

		rotate = new SpellRotate();
		rotate.setIconDisplay(new ResourceLocation(Const.MODID, "textures/spells/collect.png")).setSpellID(++spellId);
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
	}

	public static ISpell getDefaultSpell() {
		return spellbook.get(0);
	}

	public static boolean canPlayerCastAnything(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		return props.getSpellTimer() == 0;
	}

	public static void cast(ISpell spell, World world, EntityPlayer player, BlockPos pos) {
		cast(spell, world, player, pos, null, -1);
	}

	public static void cast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side, int pentity) {

		Entity target = null;
		if (pentity > 0) {
			target = world.getEntityByID(pentity);
		}

		if (spell == null) {
			System.out.println("ERROR: cast null spell");
			return;
		}
		if (canPlayerCastAnything(player) == false) {
			System.out.println("canPlayerCastAnything == false");
			return;
		}

		if (spell.canPlayerCast(world, player, pos)) {

			spell.cast(world, player, pos, side, target);
			spell.onCastSuccess(world, player, pos);
			startSpellTimer(player, spell.getCastCooldown());
		} else {
			System.out.println("onCastFailure " + spell.getSpellID());
			spell.onCastFailure(world, player, pos);
		}
	}

	public static void cast(int spell_id, World world, EntityPlayer player, BlockPos pos) {
		// ISpell sp = SpellRegistry.getSpellFromType(spell_id);
		ISpell sp = SpellRegistry.getSpellFromID(spell_id);
		cast(sp, world, player, pos);
	}

	public static void shiftLeft(EntityPlayer player) {
		ISpell current = getPlayerCurrentISpell(player);

		if (current.left() != null) {
			setPlayerCurrentSpell(player, current.left().getSpellID());
			UtilSound.playSoundAt(player, "random.orb");
		}
	}

	public static void shiftRight(EntityPlayer player) {
		ISpell current = getPlayerCurrentISpell(player);

		if (current.right() != null) {
			setPlayerCurrentSpell(player, current.right().getSpellID());
			UtilSound.playSoundAt(player, "random.orb");
		}
	}

	private static void setPlayerCurrentSpell(EntityPlayer player, int current_id) {
		PlayerPowerups props = PlayerPowerups.get(player);

		props.setSpellCurrent(current_id);
	}

	public static int getPlayerCurrentSpell(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);

		return props.getSpellCurrent();
	}

	public static int getSpellTimer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		return props.getSpellTimer();
	}

	public static void startSpellTimer(EntityPlayer player, int cooldown) {
		PlayerPowerups props = PlayerPowerups.get(player);
		props.setSpellTimer(cooldown);
	}

	public static void tickSpellTimer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		if (props.getSpellTimer() < 0)
			props.setSpellTimer(0);
		else if (props.getSpellTimer() > 0)
			props.setSpellTimer(props.getSpellTimer() - 1);
	}

	public static ISpell getPlayerCurrentISpell(EntityPlayer player) {
		int spell_id = getPlayerCurrentSpell(player);

		for (ISpell sp : spellbook) {
			// if(sp.getSpellName().equalsIgnoreCase(s))
			if (sp.getSpellID() == spell_id) {
				return sp;
			}
		}
		// if current spell is null,default to the first one

		return SpellRegistry.getDefaultSpell();
	}

	public static ISpell getSpellFromID(int id) {
		if (id == 0) {
			return null;
		}
		for (ISpell sp : spellbook) {
			if (sp.getSpellID() == id) {
				return sp;
			}
		}

		return null;
	}

	//TODO: split above into [spell registry] [spell caster] [spell renderer] 
	
	private static void drawSpellHeader(EntityPlayerSP player, ISpell spellCurrent) {
		int dim = 12;

		int x = 12, y = 2;

		// draw header
		if (SpellRegistry.canPlayerCastAnything(player)) {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x, y, dim);
		} else {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x, y, dim);
		}
	}

	private static final int ymain = 14;
	private static final int xmain = 10;
	private static final int spellSize = 16;

	private static void drawCurrentSpell(EntityPlayerSP player, ISpell spellCurrent) {

		if (spellCurrent.getIconDisplay() != null) {
			
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}
	private static void drawPrevSpells(EntityPlayerSP player, ISpell spellCurrent) {

		ISpell spellPrev = spellCurrent.right();
		if (spellPrev != null)// && spellPrev.getIconDisplay() != null
		{
			int x = xmain + 6;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellPrev.getIconDisplay(), x, y, dim);

			ISpell sRightRight = spellPrev.right();// SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

			if (sRightRight != null && sRightRight.getIconDisplay() != null) {
				x = xmain + 6 + 4;
				y = ymain + spellSize + 14;
				dim = spellSize / 2 - 2;
				UtilTextureRender.drawTextureSquare(sRightRight.getIconDisplay(), x, y, dim);

				ISpell another = sRightRight.right();
				if (another != null) {
					x = xmain + 6 + 7;
					y = ymain + spellSize + 14 + 10;
					dim = spellSize / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
	
	}
	private static void drawNextSpells(EntityPlayerSP player, ISpell spellCurrent) {
		ISpell spellNext = spellCurrent.left();
		
		if (spellNext != null)
		{
			int x = xmain - 3;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellNext.getIconDisplay(), x, y, dim);

			ISpell sLeftLeft = spellNext.left();

			if (sLeftLeft != null && sLeftLeft.getIconDisplay() != null) {
				x = xmain - 3 - 1;
				y = ymain + spellSize + 14;
				dim = 16 / 2 - 2;
				UtilTextureRender.drawTextureSquare(sLeftLeft.getIconDisplay(), x, y, dim);

				ISpell another = sLeftLeft.left();
				if (another != null) {
					x = xmain - 3 - 3;
					y = ymain + spellSize + 14 + 10;
					dim = spellSize / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	static void drawSpellWheel(RenderGameOverlayEvent.Text event) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		ISpell spellCurrent = SpellRegistry.getPlayerCurrentISpell(player);

		drawSpellHeader(player, spellCurrent);

		drawCurrentSpell(player, spellCurrent);

		drawNextSpells(player,spellCurrent);
		
		drawPrevSpells(player,spellCurrent);
	}
}
