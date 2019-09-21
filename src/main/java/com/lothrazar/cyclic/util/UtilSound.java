package com.lothrazar.cyclic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class UtilSound {

	public static void playSound(Entity entityIn, BlockPos position, SoundEvent soundIn) {
entityIn.playSound(soundIn, 1.0F, 1.0F);
		
	}

}
