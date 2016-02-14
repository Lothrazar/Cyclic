package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public abstract class BaseSpellRange extends BaseSpell{

	protected int maxRange = 64;// TODO: config

	@Override
	public void spawnParticle(World world, EntityPlayer p, BlockPos pos) {

		UtilParticle.spawnParticleBeam(p.worldObj, EnumParticleTypes.SPELL_INSTANT, p.getPosition(), pos, 3);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {

		if(block.stepSound != null && block.stepSound.getPlaceSound() != null){
			UtilSound.playSound(world, pos, block.stepSound.getPlaceSound());
		}
		else{
			UtilSound.playSound(world, pos, UtilSound.Own.crackle);//only if we miss. but then this might not get called
		}
	}

}
