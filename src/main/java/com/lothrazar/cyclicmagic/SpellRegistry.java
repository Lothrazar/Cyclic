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
	
	public static void register() {
		int duration = Const.TICKS_PER_SEC * 20;
		
		int spellId = 0;

		//used to be public statics
		BaseSpellExp ghost;
		SpellExpPotion jump;
		BaseSpellExp phase;
		SpellExpPotion slowfall;
		SpellExpPotion waterwalk;
		SpellExpPotion haste;
		SpellCollect collect;
		BaseSpellExp rotate;
		BaseSpellExp piston;
		SpellTorchProjectile torch;
		
		//then use ++spellId -> first thing is 1
		spellbook = new ArrayList<ISpell>();
		//deposit = new SpellChestDeposit();
		//deposit.setExpCost(ModMain.cfg.deposit).setSpellID(++spellId);
		
		ghost = new SpellGhost();
		ghost.setExpCost(ModMain.cfg.ghost).setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/ghost.png")).setSpellID(++spellId);
		spellbook.add(ghost);
		
		jump = new SpellExpPotion();
		jump.setPotion(Potion.jump.id,duration,PotionRegistry.V).setExpCost(ModMain.cfg.jump).setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/jump.png")).setSpellID(++spellId);
		spellbook.add(jump);
		
		phase = new SpellPhasing();
		phase.setExpCost(ModMain.cfg.phase).setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/phasing.png")).setSpellID(++spellId);
		spellbook.add(phase);
		
		slowfall = new SpellExpPotion();
		slowfall.setPotion(PotionRegistry.slowfall.id,duration,PotionRegistry.I);
		slowfall.setExpCost(ModMain.cfg.slowfall);
		slowfall.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/slowfall.png"));
		slowfall.setSpellID(++spellId);
		spellbook.add(slowfall);
		
		waterwalk = new SpellExpPotion();
		waterwalk.setPotion(PotionRegistry.waterwalk.id,duration,PotionRegistry.I).setExpCost(ModMain.cfg.waterwalk).setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/waterwalk.png")).setSpellID(++spellId);
		spellbook.add(waterwalk);
		
		haste = new SpellExpPotion();
		haste.setPotion(Potion.digSpeed.id,duration,PotionRegistry.II).setExpCost(ModMain.cfg.haste).setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/haste.png")).setSpellID(++spellId);
		spellbook.add(haste);
		
		collect = new SpellCollect();
		collect.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/collect.png")).setSpellID(++spellId);
		spellbook.add(collect);
		
		rotate = new SpellRotate();
		rotate.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/collect.png")).setSpellID(++spellId);
		spellbook.add(rotate);

		piston = new SpellPiston();
		piston.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/piston.png")).setSpellID(++spellId);
		spellbook.add(piston);

		//TODO: do we need a projectile base class?
		torch = new SpellTorchProjectile();
		torch.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/torch.png")).setSpellID(++spellId);
		spellbook.add(torch);

		SpellFishProjectile fishing = new SpellFishProjectile();
		fishing.setIconDisplay(new ResourceLocation(Const.MODID,"textures/spells/fishing.png")).setSpellID(++spellId);
		spellbook.add(fishing);
		
		
	}

	public static ArrayList<ISpell> spellbook;
	//public static BaseSpellExp deposit;
	// public static ISpell chesttransp;

	public static ISpell getDefaultSpell() {
		return spellbook.get(0);
	}

	public static final int SPELL_TOGGLE_HIDE = 0;
	public static final int SPELL_TOGGLE_SHOW = 1;

	public static boolean canPlayerCastAnything(EntityPlayer player) {
		//TODO: allow each spell has its own cooldown timer
		PlayerPowerups props = PlayerPowerups.get(player);
		return props.getSpellTimer() == 0;
	}

	public static void cast(ISpell spell, World world, EntityPlayer player, BlockPos pos) {
		cast(spell,world,player,pos,null,-1);
	}
	public static void cast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side, int pentity) {
		System.out.println("SpellRegistry.cast");
		
		Entity target = null;
		if(pentity > 0){
			target = world.getEntityByID(pentity);
		}
		
		if (spell == null) {
			System.out.println("ERROR: cast null spell");
			return;
		}
		if (canPlayerCastAnything(player) == false) {
			System.out.println("ERROR: canPlayerCastAnything == false");
			return;
		}

		if (spell.canPlayerCast(world, player, pos)) {
			System.out.println("cast " + spell.getSpellID());
			spell.cast(world, player, pos,side,target);
			spell.onCastSuccess(world, player, pos);
			startSpellTimer(player,spell.getCastCooldown());
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

	public static void startSpellTimer(EntityPlayer player,int cooldown) {
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

	@SideOnly(Side.CLIENT)
	static void drawSpell(RenderGameOverlayEvent.Text event) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		ISpell spell = SpellRegistry.getPlayerCurrentISpell(player);

		int ymain = 12;
		int dim = 12;

		int x = 12, y = 2;

		// draw header
		if (SpellRegistry.canPlayerCastAnything(player)) {
			UtilTextureRender.drawTextureSquare(spell.getIconDisplayHeaderEnabled(), x, y, dim);
		} else {
			UtilTextureRender.drawTextureSquare(spell.getIconDisplayHeaderDisabled(), x, y, dim);
		}

		// int ysmall = ymain - 3;
		int xmain = 10;
		ymain = 14;
		if (spell.getIconDisplay() != null) {
			x = xmain;
			y = ymain;
			dim = 16;
			UtilTextureRender.drawTextureSquare(spell.getIconDisplay(), x, y, dim);
		}

		ISpell spellNext = spell.left();// SpellRegistry.getSpellFromType(spell.getSpellID().next());
		ISpell spellPrev = spell.right();// SpellRegistry.getSpellFromType(spell.getSpellID().prev());

		if (spellNext != null)// && spellNext.getIconDisplay() != null
		{
			x = xmain - 3;
			y = ymain + 16;
			dim = 16 / 2;
			UtilTextureRender.drawTextureSquare(spellNext.getIconDisplay(), x, y, dim);

			ISpell sLeftLeft = spellNext.left();// SpellRegistry.getSpellFromType(spellNext.getSpellID().next());

			if (sLeftLeft != null && sLeftLeft.getIconDisplay() != null) {
				x = xmain - 3 - 1;
				y = ymain + 16 + 14;
				dim = 16 / 2 - 2;
				UtilTextureRender.drawTextureSquare(sLeftLeft.getIconDisplay(), x, y, dim);

				ISpell another = sLeftLeft.left();
				if (another != null) {
					x = xmain - 3 - 3;
					y = ymain + 16 + 14 + 10;
					dim = 16 / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
		if (spellPrev != null)// && spellPrev.getIconDisplay() != null
		{
			x = xmain + 6;
			y = ymain + 16;
			dim = 16 / 2;
			UtilTextureRender.drawTextureSquare(spellPrev.getIconDisplay(), x, y, dim);

			ISpell sRightRight = spellPrev.right();// SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

			if (sRightRight != null && sRightRight.getIconDisplay() != null) {
				x = xmain + 6 + 4;
				y = ymain + 16 + 14;
				dim = 16 / 2 - 2;
				UtilTextureRender.drawTextureSquare(sRightRight.getIconDisplay(), x, y, dim);

				ISpell another = sRightRight.right();
				if (another != null) {
					x = xmain + 6 + 7;
					y = ymain + 16 + 14 + 10;
					dim = 16 / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
	}

}
