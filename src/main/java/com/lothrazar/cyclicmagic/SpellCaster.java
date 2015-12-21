package com.lothrazar.cyclicmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellCaster {

	public static boolean isBlockedBySpellTImer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		return !(props.getSpellTimer() == 0);
	}
	public static boolean isBlockedBySpellTImer(PlayerPowerups props) { 
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
		//ISpell current = getPlayerCurrentISpell(player);
		PlayerPowerups props = PlayerPowerups.get(player);


		int left = props.prevId(props.getSpellCurrent());

		System.out.println("set left "+props.getSpellCurrent()+ " -> "+left);
		props.setSpellCurrent(left);
		UtilSound.playSoundAt(player, UtilSound.orb );
		/*
		ISpell left = SpellRegistry.getSpellFromID(props.prevId(props.getSpellCurrent()));//SpellRegistry.left(current);
		
		if (left != null) {
			setCurrentFor(player,left);
		}*/
	}

	public static void shiftRight(EntityPlayer player) {
		//ISpell current = getPlayerCurrentISpell(player);
		PlayerPowerups props = PlayerPowerups.get(player);
		
		//ISpell right = SpellRegistry.getSpellFromID(props.nextId(props.getSpellCurrent()));//SpellRegistry.left(current);
		
		int right = props.nextId(props.getSpellCurrent());
		System.out.println("set right "+props.getSpellCurrent()+ " -> "+right);

		props.setSpellCurrent(right);
		UtilSound.playSoundAt(player, UtilSound.orb );
		//SpellRegistry.right(current);
/*
		if (right != null) {
			setCurrentFor(player,right);
		}*/
	}
 
	/*
	private static void setCurrentFor(EntityPlayer player, ISpell newCurrent){

		PlayerPowerups props = PlayerPowerups.get(player);

		props.setSpellCurrent(newCurrent.getID());
		UtilSound.playSoundAt(player, UtilSound.orb );
	}*/

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
			current = SpellRegistry.getDefaultSpell();
		}

		return current;
	}

	public static void toggleUnlock(EntityPlayer player, int spell_id) {

		PlayerPowerups props = PlayerPowerups.get(player);
		
		//System.out.println(props.isSpellUnlocked(spell_id) + "  is current state");

		props.toggleOneSpell(spell_id);
		
		System.out.println(props.isSpellUnlocked(spell_id) + "  is NEW state");
	}
}
