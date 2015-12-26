package com.lothrazar.cyclicmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellCaster {

	final float MAXMANA = 100;
	
	public boolean isBlockedBySpellTImer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		return !(props.getSpellTimer() == 0);
	}
	public boolean isBlockedBySpellTImer(PlayerPowerups props) { 
		return !(props.getSpellTimer() == 0);
	}
	public boolean tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		return tryCast(SpellRegistry.caster.getPlayerCurrentISpell(player),world,player,pos,side);
	}

	public boolean tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

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

	public void shiftLeft(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);

		int left = props.prevId(props.getSpellCurrent());

		props.setSpellCurrent(left);
		UtilSound.playSoundAt(player, UtilSound.orb );
	}

	public void shiftRight(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);
		
		int right = props.nextId(props.getSpellCurrent());
	
		props.setSpellCurrent(right);
		UtilSound.playSoundAt(player, UtilSound.orb );
	}
 
	public void tickSpellTimer(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		if (props.getSpellTimer() < 0) {
			props.setSpellTimer(0);
		}
		else if (props.getSpellTimer() > 0) {
			props.setSpellTimer(props.getSpellTimer() - 1);
		}
	}

	public ISpell getPlayerCurrentISpell(EntityPlayer player) {

		PlayerPowerups props = PlayerPowerups.get(player);

		ISpell current = SpellRegistry.getSpellFromID(props.getSpellCurrent());

		if (current == null) {
			current = SpellRegistry.getDefaultSpell();
		}

		return current;
	}

	public void toggleUnlock(EntityPlayer player, int spell_id) {

		PlayerPowerups props = PlayerPowerups.get(player);
		
		props.toggleOneSpell(spell_id);
	}
	final int rechargeCost = 100;
	final int rechargeAmt = 10;
	public void rechargeWithExp(EntityPlayer player) {
		PlayerPowerups props = PlayerPowerups.get(player);
		
		if(player.capabilities.isCreativeMode){ //always set full
			PlayerPowerups.get(player).setMana((int)MAXMANA);
		}
		else if(rechargeCost < UtilExperience.getExpTotal(player) && props.getMana() + rechargeAmt <= MAXMANA){

			props.rechargeManaBy(rechargeAmt);

			UtilExperience.drainExp(player, rechargeCost);
			UtilSound.playSoundAt(player, UtilSound.portal);
		} 
		else{
			System.out.println("cannot recharge");
			UtilSound.playSoundAt(player, UtilSound.fizz);
		}
	}
}
