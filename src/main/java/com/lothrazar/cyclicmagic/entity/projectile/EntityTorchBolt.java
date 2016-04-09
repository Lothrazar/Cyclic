package com.lothrazar.cyclicmagic.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTorchBolt extends EntityThrowable{

	public static boolean damageEntityOnHit;

	public EntityTorchBolt(World worldIn){

		super(worldIn);
	}

	public EntityTorchBolt(World worldIn, EntityLivingBase ent){

		super(worldIn, ent);
	}

	public EntityTorchBolt(World worldIn, double x, double y, double z){

		super(worldIn, x, y, z);
	}

	@Override
	protected void onImpact(RayTraceResult mop){

		if(mop.entityHit != null){

			// do the snowball damage, which should be none. put out the fire
			if(damageEntityOnHit)
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);

		}

		BlockPos pos = mop.getBlockPos();
		BlockPos offset = null;

		if(mop.sideHit != null){

			offset = pos.offset(mop.sideHit);
		}

		if(this.isInWater() == false && offset != null && this.worldObj.isAirBlock(offset) && !this.worldObj.isRemote){
			// http://minecraft.gamepedia.com/Torch#Block_data
			int faceEast = 1;
			int faceWest = 2;
			int faceSouth = 3;
			int faceNorth = 4;
			int faceUp = 5;
			int blockdata;

			switch(mop.sideHit){
			case WEST:
				blockdata = faceWest;
				break;
			case EAST:
				blockdata = faceEast;
				break;
			case NORTH:
				blockdata = faceNorth;
				break;
			case SOUTH:
				blockdata = faceSouth;
				break;
			default:
				blockdata = faceUp;
				break;
			}

			this.worldObj.setBlockState(offset, Blocks.torch.getStateFromMeta(blockdata));
		}

		this.setDead();
	}
}