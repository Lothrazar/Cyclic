package com.lothrazar.cyclicmagic.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityTorchBolt extends EntityThrowable {
	private static boolean damageEntityOnHit = true;

	public EntityTorchBolt(World worldIn) {
		super(worldIn);
	}
	
	public EntityTorchBolt(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntityTorchBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "torchbolt";
	public static Item item = null;

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null) {
			// do the snowball damage, which should be none. put out the fire
			if (damageEntityOnHit)
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
		}

		BlockPos pos = mop.getBlockPos();
		BlockPos offset = null;

		if (mop.sideHit != null) {
			offset = pos.offset(mop.sideHit);
		}

		if (this.isInWater() == false && offset != null && this.worldObj.isAirBlock(offset) && !this.worldObj.isRemote) {
			// http://minecraft.gamepedia.com/Torch#Block_data
			int faceEast = 1;
			int faceWest = 2;
			int faceSouth = 3;
			int faceNorth = 4;
			int faceUp = 5;
			int blockdata;

			switch (mop.sideHit) {
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