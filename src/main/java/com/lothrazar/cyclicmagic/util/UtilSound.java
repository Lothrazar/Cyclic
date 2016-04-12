package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilSound {

	public static final float		volume				= 1.0F;
	public static final float		pitch					= 1.0F;
	public static final boolean	distanceDelay	= false;

	public static final String	snow					= "dig.snow";
	public static final String	splash				= "game.neutral.swim.splash";
	public static final String	shears				= "mob.sheep.shear";

	public static final String	portal				= "mob.endermen.portal";
	public static final String	drink					= "random.drink";

	// public static final String click = "random.wood_click";

	public static class Own {

		// internal sounds added by mod
		// all are created by ME using a third party tool: http://www.bfxr.net/
		// then converted from wav to ogg with https://online-audio-converter.com/
		public static final String	bip			= "bip";		// spell rotate
		public static final String	buzzp		= "buzzp";	// spell cast failure. water
		                                                // bolt
		// hit in nether. portal
		// waypoint x dimension. wand
		// xp refil fail
		public static final String	bwoaaap	= "bwoaaap";// used for launch spell

		public static final String	fill		= "fill";		// fill wand energy with xp

		public static final String	pew			= "pew";		// thrown spells

		public static final String	crackle	= "crackle";// scaffolding

		public static final String	thunk		= "thunk";	// chest sack create

	}

	public static SoundEvent bwoaaap;

	public static void playSound(Entity player, String sound) {

		// for client only, or if you have client/server both
		// player.playSound(sound, volume, pitch);
		System.out.println("TODO: fix sounds P" + sound);
	}

	public static void playSound(World world, BlockPos pos, String sound) {

		// works if you play it only on the server
		// world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), sound, volume,
		// pitch);
		System.out.println("TODO: fix sounds W" + sound);

	}

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
}
