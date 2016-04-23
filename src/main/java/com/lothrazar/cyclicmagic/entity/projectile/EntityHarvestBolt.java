package com.lothrazar.cyclicmagic.entity.projectile;

import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHarvestBolt extends EntityThrowable// EntitySnowball
{

	public static int			range_main		= 6;
	public static int			range_offset	= 4;

	public EntityHarvestBolt(World worldIn) {

		super(worldIn);
	}

	public EntityHarvestBolt(World worldIn, EntityLivingBase ent) {

		super(worldIn, ent);
	}

	public EntityHarvestBolt(World worldIn, double x, double y, double z) {

		super(worldIn, x, y, z);
	}

	@Override
	protected void onImpact(RayTraceResult mop) {

		if (this.getThrower() != null && mop.sideHit != null) {
			BlockPos offset = mop.getBlockPos().offset(mop.sideHit);

			// it harvests a horizontal slice each time
			UtilHarvestCrops.harvestArea(this.worldObj, this.getThrower(), mop.getBlockPos(), range_main);
			UtilHarvestCrops.harvestArea(this.worldObj, this.getThrower(), offset, range_main);
			UtilHarvestCrops.harvestArea(this.worldObj, this.getThrower(), offset.up(), range_offset);
			UtilHarvestCrops.harvestArea(this.worldObj, this.getThrower(), offset.down(), range_offset);
		}

		this.setDead();

	}


}