package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpellRange extends BaseSpell {

	public static int maxRange = 64;

	@Override
	public void spawnParticle(World world, EntityPlayer p, BlockPos pos) {

		UtilParticle.spawnParticleBeam(p.worldObj, EnumParticleTypes.SPELL_WITCH, p.getPosition(), pos, 2);
	}

	@Override
	public void playSound(World world,EntityPlayer player, Block block, BlockPos pos) {

		if(block==null||pos==null){
			return;
		}
		/*
		System.out.println("BaseSpellRange.PlaySound at"+pos.toString());
		System.out.println("Block at"+block.getUnlocalizedName());
		System.out.println("isRemote at"+player.worldObj.isRemote);

		System.out.println("getStepSound at"+ block.getStepSound().toString());

boolean ismp = (player instanceof EntityPlayerMP);
		System.out.println("ismp"+ ismp);
		*/
		if (block != null && block.getSoundType() != null && block.getSoundType().getPlaceSound() != null) {
			
			UtilSound.playSound(player, pos, block.getSoundType().getPlaceSound());
		}
		/*
		else {
			UtilSound.playSound(player, pos, SoundRegistry.crackle);
		}*/
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer p, BlockPos pos) {

		return p.capabilities.allowEdit;
	}
}
