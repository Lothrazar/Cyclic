package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public abstract class BaseSpellThrown extends BaseSpell{

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos){

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos){

		UtilSound.playSound(world, pos, UtilSound.Own.pew);
	}
}
