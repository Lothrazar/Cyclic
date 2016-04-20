package com.lothrazar.cyclicmagic.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SoundRegistry {

	public static SoundEvent crackle;
	public static SoundEvent basey;
	public static SoundEvent bip;
	public static SoundEvent buzzp;
	public static SoundEvent bwewe;
	public static SoundEvent bwoaaap;
	public static SoundEvent byeaa;
	public static SoundEvent dcoin;
	public static SoundEvent fill;
	public static SoundEvent pew;
	public static SoundEvent pow;
	public static SoundEvent thunk;
	
	public static void register() {

		basey = registerSound("basey");
		bip = registerSound("bip");
		buzzp = registerSound("buzzp");
		bwewe = registerSound("bwewe");
		bwoaaap = registerSound("bwoaaap");
		byeaa = registerSound("byeaa");
		crackle = registerSound("crackle");
		dcoin = registerSound("dcoin");
		fill = registerSound("fill");
		pew = registerSound("pew");
		pow = registerSound("pow");
		thunk = registerSound("thunk");
	}
	
	private static SoundEvent registerSound(String name){

		//thanks for the help: https://github.com/Choonster/TestMod3/tree/162914a163c7fcb6bdd992917fcbc699584e40de/src/main/java/com/choonster/testmod3
		// and http://www.minecraftforge.net/forum/index.php?topic=38076.0
		
		final ResourceLocation res = new ResourceLocation(Const.MODID, name);//new ResourceLocation(Const.MODID, "sounds/" + UtilSound.Own.crackle+".ogg");
		SoundEvent sound = new SoundEvent(res);
		sound.setRegistryName(res);
		GameRegistry.register(sound);
		return sound;
	}
}
