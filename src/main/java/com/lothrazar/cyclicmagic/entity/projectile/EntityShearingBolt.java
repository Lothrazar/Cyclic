package com.lothrazar.cyclicmagic.entity.projectile;

import com.lothrazar.cyclicmagic.event.EventAnimalDropBuffs;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityShearingBolt extends EntityThrowable {

	public static boolean	doesShearChild;
	public static boolean	doesKnockback;

	public EntityShearingBolt(World worldIn) {

		super(worldIn);
	}

	public EntityShearingBolt(World worldIn, EntityLivingBase ent) {

		super(worldIn, ent);
	}

	public EntityShearingBolt(World worldIn, double x, double y, double z) {

		super(worldIn, x, y, z);
	}

	@Override
	protected void onImpact(RayTraceResult mop) {

		if (mop.entityHit != null && mop.entityHit instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep) mop.entityHit;

			// imported from MY unreleased/abandoned BlockShearWool.java in
			// ./FarmingBlocks/
			if (sheep.getSheared() == false && sheep.worldObj.isRemote == false) {
				// this part is the same as how EntitySheep goes, use a random number
				if (sheep.isChild() == false || // either an adult, or child that passes
				                                // config
				    (EntityShearingBolt.doesShearChild == true && sheep.isChild() == true)) {
					sheep.setSheared(true);
					int i = 1 + sheep.worldObj.rand.nextInt(3);

					for (int j = 0; j < i; ++j) {
						EntityItem entityitem = sheep.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), EventAnimalDropBuffs.sheepExtraWool, sheep.getFleeceColor().getMetadata()), 1.0F);
						entityitem.motionY += (double) (sheep.worldObj.rand.nextFloat() * 0.05F);
						entityitem.motionX += (double) ((sheep.worldObj.rand.nextFloat() - sheep.worldObj.rand.nextFloat()) * 0.1F);
						entityitem.motionZ += (double) ((sheep.worldObj.rand.nextFloat() - sheep.worldObj.rand.nextFloat()) * 0.1F);
					}

					if (doesKnockback) {
						sheep.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
					}

					BlockPos pos = sheep.getPosition();
					UtilSound.playSound(sheep.worldObj,pos, SoundEvents.entity_sheep_shear,SoundCategory.NEUTRAL);
					//worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.entity_sheep_shear, SoundCategory.PLAYERS, 1.0F, 1.0F, false);

				}
				// else we hit a child sheep and config disables that

				this.setDead();
			}
		}
		else {
			BlockPos pos = mop.getBlockPos();

			if (pos != null && worldObj.isRemote == false) {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.ender_wool)));
				this.setDead();
			}
		}
	}
}
