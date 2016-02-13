package com.lothrazar.cyclicmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellCaster {

	final int RECHARGE_EXP_COST = 10;
	final int RECHARGE_MANA_AMT = 25;
	
	public boolean isBlockedBySpellTImer(EntityPlayer player) {
		return isBlockedBySpellTImer(PlayerPowerups.get(player));
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

				castSuccess(spell, world, player, pos);
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
	
	public void castSuccess(ISpell spell, World world, EntityPlayer player, BlockPos pos){
		// succes should do things like: drain resources, play sounds
		// and particles
		spell.payCost(world, player, pos);

		PlayerPowerups.get(player).setSpellTimer(spell.getCastCooldown());
	}

	public void shiftLeft(EntityPlayer player) {

		ItemStack wand = player.getHeldItem();

		int left = ItemCyclicWand.Spells.prevId(wand,ItemCyclicWand.Spells.getSpellCurrent(wand) );

		ItemCyclicWand.Spells.setSpellCurrent(wand,left);
		
		UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.bip );
	}

	public void shiftRight(EntityPlayer player) {
		ItemStack wand = player.getHeldItem();
		
		int right = ItemCyclicWand.Spells.nextId(wand, ItemCyclicWand.Spells.getSpellCurrent(wand));
	
		ItemCyclicWand.Spells.setSpellCurrent(wand,right);
		UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.bip );
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

		ISpell current = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.getSpellCurrent(player.getHeldItem()));

		if (current == null) {
			current = SpellRegistry.getDefaultSpell();
		}

		return current;
	}

	public void rechargeWithExp(EntityPlayer player) {

		int MAX = ItemCyclicWand.Energy.getMaximum(player.getHeldItem());
		
		if(player.capabilities.isCreativeMode){ //always set full
			ItemCyclicWand.Energy.setCurrent(player.getHeldItem(),MAX);
		}
		else if(RECHARGE_EXP_COST < UtilExperience.getExpTotal(player) && 
				ItemCyclicWand.Energy.getCurrent(player.getHeldItem()) + RECHARGE_MANA_AMT <= MAX){

			ItemCyclicWand.Energy.rechargeBy(player.getHeldItem(), RECHARGE_MANA_AMT);

			UtilExperience.drainExp(player, RECHARGE_EXP_COST);
			UtilSound.playSound(player.worldObj, player.getPosition() , UtilSound.Own.fill);
		} 
		else{
			UtilSound.playSound(player.worldObj, player.getPosition() ,UtilSound.Own.buzzp);
		}
	}
}
