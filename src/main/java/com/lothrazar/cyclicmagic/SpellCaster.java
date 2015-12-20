package com.lothrazar.cyclicmagic;

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
		tryCast(SpellCaster.getPlayerCurrentISpell(player),world,player,pos,side);
	}

	public static void tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if (isBlockedBySpellTImer(player)) {
			return;
		}

		if (spell.canPlayerCast(world, player, pos)) {

			if (spell.cast(world, player, pos, side)) {

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

		ISpell left = SpellRegistry.left(current);
		
		if (left != null) {
			setCurrentFor(player,left);
		}
	}

	public static void shiftRight(EntityPlayer player) {
		ISpell current = getPlayerCurrentISpell(player);
		
		ISpell right = SpellRegistry.right(current);

		if (right != null) {
			setCurrentFor(player,right);
		}
	}
	private static void setCurrentFor(EntityPlayer player, ISpell newCurrent){

		PlayerPowerups props = PlayerPowerups.get(player);

		props.setSpellCurrent(newCurrent.getID());
		UtilSound.playSoundAt(player, UtilSound.orb );
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

	public static void toggleUnlock(EntityPlayer player, int spell_id) {

		System.out.println(spell_id+"  spell unlock TODO");

		PlayerPowerups props = PlayerPowerups.get(player);
		

		System.out.println(props.isSpellUnlocked(spell_id) + "  is current state");

		props.toggleOneSpell(spell_id);
		
		System.out.println(props.isSpellUnlocked(spell_id) + "  is NEW state");
		
	}
}
