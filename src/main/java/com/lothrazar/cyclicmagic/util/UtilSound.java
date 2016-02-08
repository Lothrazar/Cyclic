package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.Const;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class UtilSound {
	public static final float volume = 1.0F;
	public static final float pitch = 1.0F;

	public static final String snow = "dig.snow";
	public static final String splash = "game.neutral.swim.splash";
	public static final String shears = "mob.sheep.shear";
	
	
	
	public static final String fizz = "random.fizz";
	public static final String orb = "random.orb";
	public static final String portal =  "mob.endermen.portal";
	public static final String drink = "random.drink";
	//public static final String click = "random.wood_click";
	
	public static class Own{
		//internal sounds added by mod
		//all are created by ME using a third party tool: http://www.bfxr.net/
		//then converted from wav to ogg with https://online-audio-converter.com/
		public static final String bip = Const.MODID+":bip";//spell rotate
		//public static final String buzzp = Const.MODID+":buzzp";
		public static final String bwoaaap = Const.MODID+":bwoaaap";//used for launch spell
		public static final String pew = Const.MODID+":pew";//thrown spells

		public static final String crackle = Const.MODID+":crackle";//scaffolding

		public static final String thunk = Const.MODID+":thunk";//chest sack create
	}

	public static void playSoundAt(Entity player, String sound) {
		//for client only, or if you have client/server both
		player.playSound(sound, volume, pitch);
	}

	public static void playSound(World world, BlockPos pos, String sound) {
		//works if you play it only on the server
		world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), sound, volume,pitch);
	}
}
