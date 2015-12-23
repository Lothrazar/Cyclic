package com.lothrazar.cyclicmagic.projectile;

import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityFishingBolt extends EntityThrowable {
	public EntityFishingBolt(World worldIn) {
		super(worldIn);
	}

	public EntityFishingBolt(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntityFishingBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "fishingbolt";
	public static Item item = null;
	static final double plainChance = 60;
	static final double salmonChance = 25 + plainChance;// so it is between 60
														// and 85
	static final double clownfishChance = 2 + salmonChance;// so between 85 and
															// 87

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		BlockPos pos = mop.getBlockPos();
		if (pos == null) {
			return;
		}

		if (this.isInWater()) {

			EntityItem ei = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), getRandomFish());

			if (worldObj.isRemote == false) {
				worldObj.spawnEntityInWorld(ei);
			}

			UtilSound.playSoundAt(ei, UtilSound.splash);
			UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.WATER_BUBBLE, pos);
		}

		this.setDead();
	}

	private ItemStack getRandomFish() {
		ItemStack fishSpawned = null;
		// below is from MY BlockFishing.java in the unreleased ./FarmingBlocks/

		double diceRoll = rand.nextDouble() * 100;

		if (diceRoll < plainChance) {
			fishSpawned = new ItemStack(Items.fish, 1, 0); // plain
		}
		else if (diceRoll < salmonChance) {
			fishSpawned = new ItemStack(Items.fish, 1, 1);// salmon
		}
		else if (diceRoll < clownfishChance) {
			fishSpawned = new ItemStack(Items.fish, 1, 2);// clown
		}
		else {
			fishSpawned = new ItemStack(Items.fish, 1, 3);// puffer
		}

		return fishSpawned;
	}
}
