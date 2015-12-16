package com.lothrazar.cyclicmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellCaster {

	public static ISpell getDefaultSpell() {
		return SpellRegistry.spellbook.get(0);
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
		ISpell sp = getSpellFromID(spell_id);
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

		for (ISpell sp : SpellRegistry.spellbook) {
			// if(sp.getSpellName().equalsIgnoreCase(s))
			if (sp.getSpellID() == spell_id) {
				return sp;
			}
		}
		// if current spell is null,default to the first one

		return getDefaultSpell();
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
