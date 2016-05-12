package com.lothrazar.cyclicmagic.entity.projectile;

import java.util.ArrayList;

import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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

	public static final int nether = -1;

	@Override
	protected void onImpact(RayTraceResult mop) {

		if (mop.entityHit != null) {

			if (mop.entityHit instanceof EntityLivingBase) {
				EntityLivingBase e = (EntityLivingBase) mop.entityHit;

				if (e.isBurning()) {
					e.extinguish();
				}
			}
		}

		BlockPos pos = mop.getBlockPos();
		if (pos == null) {
			pos = this.getPosition();
		}

		if (pos != null) {
			// UtilParticle.spawnParticle(this.worldObj,
			// EnumParticleTypes.WATER_SPLASH, pos);

			if (this.getThrower() instanceof EntityPlayer && mop.sideHit != null && this.worldObj.isRemote == false) {

				this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
			}
		}

		if (this.dimension != nether) {

			UtilSound.playSound(worldObj, pos, SoundEvents.entity_player_splash, SoundCategory.PLAYERS);
			
			// so far its both client and server
			if (this.worldObj.isRemote == false) {

				if (pos != null) {

					if (this.isAirOrWater(pos)) {

						this.worldObj.setBlockState(pos, Blocks.flowing_water.getDefaultState(), 3);
					}
					if (mop.sideHit != null) {
						BlockPos offset = pos.offset(mop.sideHit);

						if (offset != null && this.isAirOrWater(offset)) {

							this.worldObj.setBlockState(offset, Blocks.flowing_water.getDefaultState(), 3);
						}
					}
				}
			}
		}

		this.setDead();
	}

	private boolean isAirOrWater(BlockPos pos) {

		ArrayList<Block> waterBoth = new ArrayList<Block>();
		waterBoth.add(Blocks.flowing_water);
		waterBoth.add(Blocks.water);
		if (pos == null) { return false; }
		return this.worldObj.isAirBlock(pos) || this.worldObj.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.water") || (this.worldObj.getBlockState(pos) != null && waterBoth.contains(this.worldObj.getBlockState(pos).getBlock()));
	}
}