package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SpellLaunch extends BaseSpell implements ISpell {

	private static final double	power				= 2;
	private static final double	mountPower	= power - 0.5;
	private static final int		slowfallSec	= 10;			// TODO: this 10 seconds in
	                                                  // config..??

	public SpellLaunch(int id, String name) {

		super.init(id, name);
		this.cost = 75;
		this.cooldown = 15;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {

		if(player.isSneaking()){

			PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(PotionRegistry.slowfall, slowfallSec * Const.TICKS_PER_SEC));

			return true;
		}

		double velX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
		double velZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);

		double velY = (double) (-MathHelper.sin((player.rotationPitch) / 180.0F * (float) Math.PI) * power);

		// launch the player up and forward at minimum angle
		// regardless of look vector
		if (velY < 0) {
			velY *= -1;// make it always up never down
		}
		if (velY < 0.4) {
			velY = 0.4 + player.jumpMovementFactor;
		}
		boolean isLookingDown = (player.getLookVec().yCoord < -20);
		if(isLookingDown){
			velY += 3;
		}
		
		player.motionY = 0;
		player.fallDistance = 0;

		Entity ridingEntity = player.getRidingEntity();

		if (ridingEntity != null) {
			ridingEntity.motionY = 0;
			ridingEntity.fallDistance = 0;
			// boost power a bit, horses are heavy as F
			ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);

			if (ridingEntity instanceof EntityLivingBase) {
				// if its a horse or something
				((EntityLivingBase) ridingEntity).addPotionEffect(new PotionEffect(PotionRegistry.slowfall, slowfallSec * Const.TICKS_PER_SEC));
			}
		}
		else {
			player.addVelocity(velX, velY, velZ);
		}

		//this.playSound(world, null, player.getPosition());
		//UtilSound.playSound(player, SoundRegistry.bwoaaap);
		this.playSound(world, player, null, pos);
		this.spawnParticle(world, player, player.getPosition());

		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);

	}

	@Override
	public void playSound(World world, EntityPlayer player,Block block, BlockPos pos) {

		UtilSound.playSound(player, pos, SoundRegistry.bwoaaap);

	}
}
