package com.lothrazar.cyclicmagic.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SoundRegistry {

	public static void register() {

		// TODO: fix this. didnt actually work so..
		UtilSound.bwoaaap = new SoundEvent(new ResourceLocation("cyclicmagic:sounds/bwoaaap"));
	}
}
