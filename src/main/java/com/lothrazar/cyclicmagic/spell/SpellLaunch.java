package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SpellLaunch extends BaseSpell implements ISpell {
	
	private static final float power = 1.1F;
	
	public SpellLaunch(int id,String name){
		super(id,name);
		this.cost = 5;
	}
	
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		player.motionY = 0;
		player.fallDistance = 0;
		
		double velX;
		double velY = (double) (-MathHelper.sin((player.rotationPitch) / 180.0F * (float) Math.PI) * power);
		double velZ;

		if (velY < 0) {
			velY *= -1;// make it always up never down
		}
		
		if(player.isSneaking()){
			//then do vertical only
			velX = 0;
			velZ = 0;
			
			velY *= 2 ;//and pump up the power 
		}
		else{
			velX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
			velZ = (double) ( MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI) * power);
			
			// launch the player up and forward at minimum 30 degrees
			// regardless of look vector

			if (velY < 0.4) {
				velY = 0.4 + player.jumpMovementFactor;// do a bit of a
			}
		}

		player.addVelocity(velX, velY, velZ);
		
		return true;
	}
}
