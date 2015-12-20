package com.lothrazar.cyclicmagic.projectile;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBlazeBolt extends EntityThrowable {

	public EntityBlazeBolt(World worldIn) {
		super(worldIn);
	}

	public EntityBlazeBolt(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntityBlazeBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "blazebolt";
	public static Item item = null;

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null) {

			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);

			if (mop.entityHit.isBurning() == false && mop.entityHit instanceof EntityBlaze == false) {
				mop.entityHit.setFire(2);

				UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.FLAME, mop.entityHit.getPosition());
			}
		}

		BlockPos pos = mop.getBlockPos();
		BlockPos offset = null;

		if (pos == null) {
			return;
		}// hasn't happened yet, but..

		ArrayList<Block> waterBoth = new ArrayList<Block>();
		waterBoth.add(Blocks.flowing_water);
		waterBoth.add(Blocks.water);

		if (mop.sideHit != null && this.getThrower() instanceof EntityPlayer) {
			this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);

			offset = mop.getBlockPos().offset(mop.sideHit);
		}

		// Block hitBlock = this.worldObj.getBlockState(pos).getBlock();
		if (mop.sideHit != null)
			offset = pos.offset(mop.sideHit);
		ArrayList<BlockPos> toSetFire = new ArrayList<BlockPos>();
		if (this.isInWater() == false) {

			if (this.worldObj.isAirBlock(pos)) {
				toSetFire.add(pos);
				// turn flowing water into solid
				this.worldObj.setBlockState(pos, Blocks.fire.getDefaultState());

			}
			if (this.worldObj.isAirBlock(pos.offset(EnumFacing.EAST))) {
				toSetFire.add(pos.offset(EnumFacing.EAST));
			}
			if (this.worldObj.isAirBlock(pos.offset(EnumFacing.NORTH))) {
				toSetFire.add(pos.offset(EnumFacing.NORTH));
			}
			if (this.worldObj.isAirBlock(pos.offset(EnumFacing.SOUTH))) {
				toSetFire.add(pos.offset(EnumFacing.SOUTH));
			}
			if (this.worldObj.isAirBlock(pos.offset(EnumFacing.WEST))) {
				toSetFire.add(pos.offset(EnumFacing.WEST));
			}
			if (this.worldObj.isAirBlock(pos.offset(EnumFacing.UP))) {
				toSetFire.add(pos.offset(EnumFacing.UP));
			}

			if (offset != null && this.worldObj.isAirBlock(offset)) {
				toSetFire.add(offset);
				if (this.worldObj.isAirBlock(offset.offset(EnumFacing.EAST))) {
					toSetFire.add(offset.offset(EnumFacing.EAST));
				}
				if (this.worldObj.isAirBlock(offset.offset(EnumFacing.NORTH))) {
					toSetFire.add(offset.offset(EnumFacing.NORTH));
				}
				if (this.worldObj.isAirBlock(offset.offset(EnumFacing.SOUTH))) {
					toSetFire.add(offset.offset(EnumFacing.SOUTH));
				}
				if (this.worldObj.isAirBlock(offset.offset(EnumFacing.WEST))) {
					toSetFire.add(offset.offset(EnumFacing.WEST));
				}
				if (this.worldObj.isAirBlock(offset.offset(EnumFacing.UP))) {
					toSetFire.add(offset.offset(EnumFacing.UP));
				}
			}

			for (BlockPos p : toSetFire) {
				this.worldObj.setBlockState(p, Blocks.fire.getDefaultState());

				UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.FLAME, pos);
			}
		}

		this.setDead();
	}
}