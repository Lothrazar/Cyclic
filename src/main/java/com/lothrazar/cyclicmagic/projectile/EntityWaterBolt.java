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
import net.minecraft.init.Items;
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
		if(pos == null){
			pos = this.getPosition();
		}

		if(pos != null){
			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.WATER_SPLASH, pos);
			
			if (this.getThrower() instanceof EntityPlayer && mop.sideHit != null
					&& this.worldObj.isRemote == false) {
				this.worldObj.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
			}
		}
		
		if (this.dimension == Const.Dimension.nether){
			UtilSound.playSoundAt(this, UtilSound.fizz);
		}
		else {
			UtilSound.playSoundAt(this, UtilSound.splash);
			
			//so far its both client and server
			if(this.worldObj.isRemote == false){
				System.out.println("TESTING TIME " + this.worldObj.isRemote);
				System.out.println(mop.toString());
				System.out.println(mop.getBlockPos());
				System.out.println("isAirOrWater "  + this.isAirOrWater(pos));
				System.out.println("=====");
				
				if(pos != null){
					
					if (this.isAirOrWater(pos)){
						System.out.println("set water");
						this.worldObj.setBlockState(pos, Blocks.flowing_water.getDefaultState(),3);
					}
					if(mop.sideHit != null){
						BlockPos offset = pos.offset(mop.sideHit);
						System.out.println("offset "  + this.isAirOrWater(offset));
						if (offset != null && this.isAirOrWater(offset)) {
					
							System.out.println("offset water "+ this.worldObj.isRemote );
			 
							this.worldObj.setBlockState(offset, Blocks.flowing_water.getDefaultState(),3);
						}
					}
				}
			}
		}

		this.setDead();
	}
	
	private boolean isAirOrWater(BlockPos pos){
		if(pos == null){
			return false;
		}
		return this.worldObj.isAirBlock(pos) || 
				(this.worldObj.getBlockState(pos) != null && waterBoth.contains(this.worldObj.getBlockState(pos).getBlock()));
	}
}