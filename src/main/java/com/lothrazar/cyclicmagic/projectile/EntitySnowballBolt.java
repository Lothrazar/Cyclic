package com.lothrazar.cyclicmagic.projectile;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySnowballBolt extends EntityThrowable {
	public static final int secondsFrozenOnHit = 5;
	private static final float damage = 0;

	public EntitySnowballBolt(World worldIn) {
		super(worldIn);
	}

	public EntitySnowballBolt(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntitySnowballBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "frostbolt";
	public static Item item = null;

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) {
System.out.println("hit entity");
			// if we hit an entity
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
			EntityLivingBase e = (EntityLivingBase) mop.entityHit;

			if (e.isBurning()) {
				e.extinguish();
			}
System.out.println("Add Frost Effect");
			PotionRegistry.addOrMergePotionEffect(e, new PotionEffect(PotionRegistry.frost.id, secondsFrozenOnHit * Const.TICKS_PER_SEC, 0));

			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOWBALL, e.getPosition());
			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOW_SHOVEL, e.getPosition());
			this.setDead();
		}
		else if (mop.getBlockPos() != null) {
System.out.println("hit block");
			// else if we hit a block

			BlockPos pos = mop.getBlockPos();

			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOWBALL, pos);
			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOW_SHOVEL, pos);

			if (mop.sideHit != null && this.getThrower() instanceof EntityPlayer) {
				this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
			}

			if (this.isInWater()) {
				// try to turn water into ice
				BlockPos posWater = this.getPosition();

				if (this.worldObj.getBlockState(posWater) != Blocks.water.getDefaultState()) {
					posWater = null;
					// look for the closest water source,
					// sometimes
					// it was air and we got ice right above the
					// water if we dont do this check
					if (this.worldObj.getBlockState(mop.getBlockPos()) == Blocks.water.getDefaultState()){
						posWater = mop.getBlockPos();
					}
					else if (this.worldObj.getBlockState(mop.getBlockPos().offset(mop.sideHit)) == Blocks.water.getDefaultState()){
						posWater = mop.getBlockPos().offset(mop.sideHit);
					}
				}

				if (posWater != null) {
					this.worldObj.setBlockState(posWater, Blocks.ice.getDefaultState());
				}
			}
			else {
				// put snow on the land
				BlockPos hit = pos;
				BlockPos hitDown = hit.down();
				BlockPos hitUp = hit.up();

				IBlockState hitState = this.worldObj.getBlockState(hit);
				if (hitState.getBlock() == Blocks.snow_layer) {
					setMoreSnow(this.worldObj, hit);

				}
				else if (this.worldObj.getBlockState(hitDown).getBlock() == Blocks.snow_layer) {
					setMoreSnow(this.worldObj, hitDown);
				}
				else if (this.worldObj.getBlockState(hitUp).getBlock() == Blocks.snow_layer) {
					setMoreSnow(this.worldObj, hitUp);
				}
				else if (this.worldObj.isAirBlock(hit) == false && this.worldObj.isAirBlock(hitUp) == true) {
					//this.worldObj.isSideSolid(pos, EnumFacing.UP)
					setNewSnow(this.worldObj, hitUp);
				}
				else if (this.worldObj.isAirBlock(hit) == false && this.worldObj.isAirBlock(hitUp) == true) {
					setNewSnow(this.worldObj, hitUp);
				}
			}

			// end of branch hit block
			this.setDead();
		}
	}

	private static void setMoreSnow(World world, BlockPos pos) {
		// so of the block hit, get metadata, and add +1 to it, unless its full
		// like 8 or more

		IBlockState hitState = world.getBlockState(pos);
		int m = hitState.getBlock().getMetaFromState(hitState);
		// when it hits 7, same size as full block
		if (m + 1 < 8) {
			world.setBlockState(pos, Blocks.snow_layer.getStateFromMeta(m + 1));
		}

		UtilSound.playSoundAt(world, pos, UtilSound.snow);
	}

	private static void setNewSnow(World world, BlockPos pos) {
		world.setBlockState(pos, Blocks.snow_layer.getDefaultState());

		UtilSound.playSoundAt(world, pos, UtilSound.snow);
	}
}