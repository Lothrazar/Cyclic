package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SpellLaunch extends BaseSpell implements ISpell {
	public SpellLaunch(int id,String name){
		super(id,name);
		this.cooldown = 8;
		this.durability = 50;
		this.experience = 10;
	}
	float power = 2.5F;
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		System.out.println("launch "+world.isRemote);
		player.motionY = 0;
		player.fallDistance = 0;
		
		double velX;
		double velY = (double) (-MathHelper.sin((player.rotationPitch) / 180.0F * (float) Math.PI) * power);
		double velZ;
		
		if(player.isSneaking()){
			//then do vertical only
			velX = 0;
			velZ = 0;
			if (velY < 0) {
				velY *= -1;// make it always up never down
			}
			//power = 1.8F;
			velY *= (power / 2);
		}
		else{
			velX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
			velZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
			
			// launch the player up and forward at minimum 30 degrees
			// regardless of look vector
			if (velY < 0) {
				velY *= -1;// first invert direction
			}
			if (velY < 0.4) {
				// if you are looking straight ahead, this is zero

				velY = 0.4 + player.jumpMovementFactor;// do a bit of a
			}
		}

		System.out.println(velX  +":"+ velY +":"+ velZ);
		player.addVelocity(velX, velY, velZ);
		
		return true;
	}
}
