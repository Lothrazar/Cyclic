package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SpellLaunch extends BaseSpell implements ISpell{

	private static final float power = 1.005F;
	private static final float mountPower = 1.01F;
	private static final int slowfallSec = 10;//TODO: this 10 seconds in config..??

	public SpellLaunch(int id, String name){

		super.init(id, name);
		this.cost = 10;
		this.cooldown = 10;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		double velY = (double) (-MathHelper.sin((player.rotationPitch) / 180.0F * (float) Math.PI) * power);

		if(velY < 0){
			velY *= -1;// make it always up never down
		}
		// launch the player up and forward at minimum angle
		// regardless of look vector

		if(velY < 0.4){
			velY = 0.4 + player.jumpMovementFactor;
		}

		double velX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
		double velZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);

		player.motionY = 0;
		player.fallDistance = 0;
		if(player.ridingEntity != null){
			player.ridingEntity.motionY = 0;
			player.ridingEntity.fallDistance = 0;
			// boost power a bit, horses are heavy as F
			player.ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);

			if(player.ridingEntity instanceof EntityLivingBase){
				//if its a horse or something
				((EntityLivingBase)player.ridingEntity).addPotionEffect(new PotionEffect(PotionRegistry.slowfall.id,slowfallSec * Const.TICKS_PER_SEC));
			}
		}
		else{
			player.addVelocity(velX, velY, velZ);
		}

		this.playSound(world, null, player.getPosition());
		this.spawnParticle(world, player, player.getPosition());
		
		
		player.addPotionEffect(new PotionEffect(PotionRegistry.slowfall.id,slowfallSec * Const.TICKS_PER_SEC));

		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos){

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);

	}

	@Override
	public void playSound(World world, Block block, BlockPos pos){

		UtilSound.playSound(world, pos, UtilSound.Own.bwoaaap);

	}
}
