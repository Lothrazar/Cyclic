package com.lothrazar.cyclicmagic.projectile;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWaterBolt extends EntityThrowable {
	public EntityWaterBolt(World worldIn) {
		super(worldIn);
	}

	public EntityWaterBolt(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntityWaterBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "waterbolt";
	public static Item item = null;

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null) {
			// do the snowball damage, which should be none. put out the fire

			if (mop.entityHit instanceof EntityLivingBase) {
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);

				UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.WATER_SPLASH, mop.entityHit.getPosition());

				EntityLivingBase e = (EntityLivingBase) mop.entityHit;

				if (e.isBurning()) {
					e.extinguish();
				}
			}
		}

		BlockPos pos = mop.getBlockPos();
		BlockPos offset = null;

		if (pos == null) {
			return;
		}// hasn't happened yet, but..

		UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.WATER_SPLASH, pos);

		if (this.dimension == Const.Dimension.nether) {
			UtilSound.playSoundAt(this, UtilSound.fizz);
		}
		else {
			UtilSound.playSoundAt(this, UtilSound.splash);
			ArrayList<Block> waterBoth = new ArrayList<Block>();
			waterBoth.add(Blocks.flowing_water);
			waterBoth.add(Blocks.water);

			if (mop.sideHit != null && this.getThrower() instanceof EntityPlayer) {
				this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);

				offset = mop.getBlockPos().offset(mop.sideHit);
			}

			Block hitBlock = this.worldObj.getBlockState(pos).getBlock();

			if (waterBoth.contains(hitBlock)) {
				// turn flowing water into solid
				this.worldObj.setBlockState(pos, Blocks.water.getDefaultState());
			}

			if (this.isInWater() == false) {
				if (this.worldObj.isAirBlock(pos)) {
					this.worldObj.setBlockState(pos, Blocks.water.getDefaultState());
				}
				else if (offset != null && this.worldObj.isAirBlock(mop.getBlockPos().offset(mop.sideHit))) {
					this.worldObj.setBlockState(mop.getBlockPos().offset(mop.sideHit), Blocks.water.getDefaultState());
				}
			}
		}

		this.setDead();
	}
}