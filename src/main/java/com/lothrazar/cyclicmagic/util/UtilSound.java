package com.lothrazar.cyclicmagic.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilSound {
//REF BROKEN http://www.minecraftforge.net/forum/index.php?topic=37547.0
	public static final float		volume				= 1.0F;
	public static final float		pitch					= 1.0F;
	public static final boolean	distanceDelay	= false;


	
	public static void playSoundPlaceBlock(EntityPlayer player,BlockPos pos, Block block) {
		
		if(block != null && block.getStepSound() != null){
			playSound(player ,pos,block.getStepSound().getPlaceSound(),SoundCategory.BLOCKS);
		}
	}
	public static void playSound(EntityPlayer entityPlayer, BlockPos pos,SoundEvent thunk) {
		playSound(entityPlayer,pos,thunk,entityPlayer.getSoundCategory());
	}

	public static void playSound(EntityPlayer player,BlockPos pos, SoundEvent soundIn, SoundCategory cat) {
		player.worldObj.playSound(player, pos, soundIn, cat, volume, pitch);
	}
	
	
	
	
	
	public static void playSound(World worldObj, BlockPos pos, SoundEvent soundIn, SoundCategory category) {

		worldObj.playSound(pos.getX(),pos.getY(),pos.getZ(), soundIn, category, volume, pitch, distanceDelay);
	
	}

}
