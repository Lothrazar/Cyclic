package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpellPlace extends BaseSpell{

	//just so all bulk placement spells have something in common
	
	@Override
	public void playSound(World world, Block block, BlockPos pos){

		if(block != null && block.getStepSound() != null && block.getStepSound().getPlaceSound() != null){
			UtilSound.playSound(world, pos, block.getStepSound().getPlaceSound());
		}
		else{
			UtilSound.playSound(world, pos, UtilSound.Own.crackle);
		}
	}
	

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		
	}
}
