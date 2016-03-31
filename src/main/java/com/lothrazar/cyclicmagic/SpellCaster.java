package com.lothrazar.cyclicmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand.Energy;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellCaster{
	
	public static ItemStack getPlayerWandIfHeld(EntityPlayer player){
		
		ItemStack wand = player.getHeldItemMainhand();
		if(wand != null && wand.getItem() instanceof ItemCyclicWand){
			return wand;
		}
		wand = player.getHeldItemOffhand();
		if(wand != null && wand.getItem() instanceof ItemCyclicWand){
			return wand;
		}
		
		return null;
	}


	public boolean tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		return tryCast(SpellRegistry.caster.getPlayerCurrentISpell(player), world, player, pos, side);
	}

	public boolean tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		ItemStack wand = getPlayerWandIfHeld(player);
		if(wand == null){
			return false;
		}
		
		if(ItemCyclicWand.Timer.isBlockedBySpellTimer(wand)){
			return false;
		}

		if(spell.canPlayerCast(world, player, pos)){

			if(spell.cast(world, player, pos, side)){

				castSuccess(spell, world, player, pos);
				
				return true;
			}
			return false;
			// else the spell was cast, but it had no result
			// failure does not trigger here. it was cast just didnt work
			// so maybe just was no valid target, or position was blocked/in use
		}
		else{
			// not enough XP (resources)
			spell.onCastFailure(world, player, pos);
			return false;
		}
	}

	public void castSuccess(ISpell spell, World world, EntityPlayer player, BlockPos pos){

		// succes should do things like: drain resources, play sounds
		// and particles
		spell.payCost(world, player, pos);
		
		ItemCyclicWand.Energy.setCooldownCounter(getPlayerWandIfHeld(player), world.getTotalWorldTime());

		ItemCyclicWand.Timer.setSpellTimer(getPlayerWandIfHeld(player),spell.getCastCooldown());
	}

	public void shiftLeft(EntityPlayer player){

		ItemStack wand = getPlayerWandIfHeld(player);

		int left = ItemCyclicWand.Spells.prevId(wand, ItemCyclicWand.Spells.getSpellCurrent(wand));

		ItemCyclicWand.Spells.setSpellCurrent(wand, left);

		UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.bip);
	}

	public void shiftRight(EntityPlayer player){

		ItemStack wand = getPlayerWandIfHeld(player);

		int right = ItemCyclicWand.Spells.nextId(wand, ItemCyclicWand.Spells.getSpellCurrent(wand));

		ItemCyclicWand.Spells.setSpellCurrent(wand, right);
		UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.bip);
	}


	public ISpell getPlayerCurrentISpell(EntityPlayer player){
		ItemStack wand = getPlayerWandIfHeld(player);

		ISpell current = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.getSpellCurrent(wand));

		if(current == null){
			current = SpellRegistry.getDefaultSpell();
		}

		return current;
	}

	public void rechargeWithExp(EntityPlayer player){
		ItemStack wand = getPlayerWandIfHeld(player);
		
		
		int MAX = ItemCyclicWand.Energy.getMaximum(wand);

		if(player.capabilities.isCreativeMode){ // always set full
			ItemCyclicWand.Energy.setCurrent(wand, MAX);
		}
		else if(Energy.RECHARGE_EXP_COST < UtilExperience.getExpTotal(player) && ItemCyclicWand.Energy.getCurrent(wand) + Energy.RECHARGE_MANA_AMT <= MAX){

			ItemCyclicWand.Energy.rechargeBy(wand, Energy.RECHARGE_MANA_AMT);

			UtilExperience.drainExp(player, Energy.RECHARGE_EXP_COST);
			UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.fill);
		}
		else{
			UtilSound.playSound(player.worldObj, player.getPosition(), UtilSound.Own.buzzp);
		}
	}
}
