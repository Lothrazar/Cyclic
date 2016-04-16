package com.lothrazar.cyclicmagic.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SoundRegistry {

	public static void register() {

		// TODO: fix this. didnt actually work so..
	
		ResourceLocation rl = new ResourceLocation("cyclicmagic:sounds/bwoaaap.ogg");
		UtilSound.bwoaaap = new SoundEvent(rl);
		
		GameRegistry.register(UtilSound.bwoaaap,rl);
	}
}
