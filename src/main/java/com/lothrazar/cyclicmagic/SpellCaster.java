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
	public static boolean tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		return tryCast(SpellCaster.getPlayerCurrentISpell(player),world,player,pos,side);
	}

	public static boolean tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if(world.isRemote){
			System.out.println("cancel casting client side");
			return false;
		}
		if (isBlockedBySpellTImer(player)) {
			return false;
		}

		if (spell.canPlayerCast(world, player, pos)) {

			if (spell.cast(world, player, pos, side)) {

				// succes should do things like: drain resources, play sounds
				// and particles
				spell.onCastSuccess(world, player, pos);

				PlayerPowerups props = PlayerPowerups.get(player);
				props.setSpellTimer(spell.getCastCooldown());
				return true;
			}
			return false;
			//else the spell was cast, but it had no result			
			// failure does not trigger here. it was cast just didnt work
			// so maybe just was no valid target, or position was blocked/in use
		}
		else {
			// not enough XP (resources)
			spell.onCastFailure(world, player, pos);
			return false;
		}
	}

	public static void shiftLeft(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);

		int left = props.prevId(props.getSpellCurrent());

		props.setSpellCurrent(left);
		UtilSound.playSoundAt(player, UtilSound.orb );
	}

	public static void shiftRight(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);
		
		int right = props.nextId(props.getSpellCurrent());
		System.out.println("set right "+props.getSpellCurrent()+ " -> "+right);

		props.setSpellCurrent(right);
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
			current = SpellRegistry.getDefaultSpell();
		}

		return current;
	}

	public static void toggleUnlock(EntityPlayer player, int spell_id) {

		PlayerPowerups props = PlayerPowerups.get(player);
		
		props.toggleOneSpell(spell_id);
	}
}
