package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public abstract class BaseSpellRange extends BaseSpell {

	public static int maxRange = 64;// TODO: config

	@Override
	public void spawnParticle(World world, EntityPlayer p, BlockPos pos) {

		UtilParticle.spawnParticleBeam(p.worldObj, EnumParticleTypes.SPELL_WITCH, p.getPosition(), pos, 2);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {

		if (block != null && block.getStepSound() != null && block.getStepSound().getPlaceSound() != null) {
			UtilSound.playSound(world, pos, block.getStepSound().getPlaceSound());
		}
		else {
			UtilSound.playSound(world, pos, UtilSound.Own.crackle);
		}
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer p, BlockPos pos) {

		return p.capabilities.allowEdit;
	}
}
