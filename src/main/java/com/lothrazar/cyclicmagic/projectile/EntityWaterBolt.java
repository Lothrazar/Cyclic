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
	ArrayList<Block> waterBoth = new ArrayList<Block>();
	public EntityWaterBolt(World worldIn) {
		super(worldIn);
		waterBoth.add(Blocks.flowing_water);
		waterBoth.add(Blocks.water);
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
			
			if(mop.sideHit != null ){
				offset = mop.getBlockPos().offset(mop.sideHit);
				
				if (this.getThrower() instanceof EntityPlayer) {
					this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
				}
			}

			if (this.isAirOrWater(pos)) {
				this.worldObj.setBlockState(pos, Blocks.water.getDefaultState());
			}
			else if (offset != null && this.isAirOrWater(offset)) {

				this.worldObj.setBlockState(offset, Blocks.water.getDefaultState());
			}
		}

		this.setDead();
	}
	
	private boolean isAirOrWater(BlockPos pos){
		return this.worldObj.isAirBlock(pos) || 
				(this.worldObj.getBlockState(pos) != null && waterBoth.contains(this.worldObj.getBlockState(pos).getBlock()));
	}
}