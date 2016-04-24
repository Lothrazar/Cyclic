package com.lothrazar.cyclicmagic.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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

	public static final String	snow					= "dig.snow";
	public static final String	splash				= "game.neutral.swim.splash";
	public static final String	shears				= "mob.sheep.shear";

	public static final String	portal				= "mob.endermen.portal";
	public static final String	drink					= "random.drink";

	public static void playSound(Entity entity, SoundEvent soundIn) {
		playSound(entity.getEntityWorld(), entity.getPosition(), soundIn);
	}

	public static void playSound(World world, BlockPos pos, SoundEvent soundIn) {

		playSound(world,pos,soundIn,SoundCategory.PLAYERS);
	}
	
	public static void playSound(World worldObj, BlockPos pos, SoundEvent soundIn, SoundCategory cat) {
		worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), soundIn,cat, volume, pitch, distanceDelay);
	}

	public static void playSound(EntityPlayer player, SoundEvent soundIn, SoundCategory cat) {

		BlockPos pos = player.getPosition();
		playSound(	player.worldObj,pos, soundIn, cat);
	}

	public static void playSoundPlaceBlock(EntityPlayer player, Block block) {
		
		if(block != null && block.getStepSound() != null)
			UtilSound.playSound(player ,block.getStepSound().getPlaceSound());
		
	}
}
