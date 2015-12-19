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

	public static boolean isBlockedBySpellTImer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		return !(props.getSpellTimer() == 0);
	}
	public static void tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		tryCast(SpellCaster.getPlayerCurrentISpell(player),world,player,pos,side,-1);
	}
	public static void tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side, int pentity) {
		tryCast(SpellCaster.getPlayerCurrentISpell(player),world,player,pos,side,pentity);
	}
	public static void tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side, int pentity) {

		Entity target = null;
		if (pentity > 0) {
			target = world.getEntityByID(pentity);
		}

		if (isBlockedBySpellTImer(player)) {
			return;
		}

		if (spell.canPlayerCast(world, player, pos)) {

			if (spell.cast(world, player, pos, side, target)) {

				// succes should do things like: drain resources, play sounds
				// and particles
				spell.onCastSuccess(world, player, pos);

				PlayerPowerups props = PlayerPowerups.get(player);
				props.setSpellTimer(spell.getCastCooldown());
			}
			//else the spell was cast, but it had no result			
			// failure does not trigger here. it was cast just didnt work
			// so maybe just was no valid target, or position was blocked/in use
		}
		else {
			// not enough XP (resources)
			spell.onCastFailure(world, player, pos);
		}
	}

	public static void shiftLeft(EntityPlayer player) {
		ISpell current = getPlayerCurrentISpell(player);

		if (current.left() != null) {
			PlayerPowerups props = PlayerPowerups.get(player);

			props.setSpellCurrent(current.left().getSpellID());
			UtilSound.playSoundAt(player, UtilSound.orb );
		}
	}

	public static void shiftRight(EntityPlayer player) {
		ISpell current = getPlayerCurrentISpell(player);

		if (current.right() != null) {
			PlayerPowerups props = PlayerPowerups.get(player);

			props.setSpellCurrent(current.right().getSpellID());
			UtilSound.playSoundAt(player, UtilSound.orb );
		}
	}

	public static void tickSpellTimer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		if (props.getSpellTimer() < 0) {
			props.setSpellTimer(0);
		}
		else if (props.getSpellTimer() > 0) {
			props.setSpellTimer(props.getSpellTimer() - 1);
		}
	}

	public static ISpell getPlayerCurrentISpell(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);

		ISpell current = SpellRegistry.getSpellFromID(props.getSpellCurrent());

		if (current == null) {
			current = getDefaultSpell();
		}

		return current;
	}
}
