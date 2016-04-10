package com.lothrazar.cyclicmagic.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHomeBolt extends EntityThrowable {

	public EntityHomeBolt(World worldIn) {

		super(worldIn);
	}

	public EntityHomeBolt(World worldIn, EntityLivingBase ent) {

		super(worldIn, ent);
	}

	public EntityHomeBolt(World worldIn, double x, double y, double z) {

		super(worldIn, x, y, z);
	}

	@Override
	protected void onImpact(RayTraceResult mop) {

		if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer && this.getThrower().dimension == 0) {
			EntityPlayer player = (EntityPlayer) this.getThrower();

			BlockPos realBedPos = getBedLocationSafe(worldObj, player);

			if (realBedPos == null) {
				realBedPos = worldObj.getSpawnPoint();
			}

			teleportWallSafe(player, worldObj, realBedPos);
			worldObj.playSound(realBedPos.getX(), realBedPos.getY(), realBedPos.getZ(), SoundEvents.entity_endermen_teleport, SoundCategory.PLAYERS, 1.0F, 1.0F, false);

			this.setDead();
		}
	}

	public static BlockPos getBedLocationSafe(World world, EntityPlayer player) {

		BlockPos realBedPos = null;

		BlockPos coords = player.getBedLocation(0);

		if (coords != null) {
			IBlockState state = world.getBlockState(coords);
			Block block = state.getBlock();

			if (block.equals(Blocks.bed) || block.isBed(state, world, coords, player)) {
				// then move over according to how/where the bed wants me to spawn
				realBedPos = block.getBedSpawnPosition(state, world, coords, player);
			}
		}

		return realBedPos;
	}

	public static void teleportWallSafe(EntityLivingBase player, World world, BlockPos coords) {

		player.setPositionAndUpdate(coords.getX(), coords.getY(), coords.getZ());

		moveEntityWallSafe(player, world);
	}

	public static void moveEntityWallSafe(EntityLivingBase entity, World world) {

		while (
		// make a 2 high space for player
		world.isAirBlock(entity.getPosition()) == false && world.isAirBlock(entity.getPosition().up()) == false
		// entity.getEntityBoundingBox() != null && //gm 3 must have a null box
		// because of the
		// ghost

		// intersects?
		// !world.getCollidingBoundingBoxes(entity,
		// entity.getEntityBoundingBox()).isEmpty()
		) {

			entity.setPositionAndUpdate(entity.posX, entity.posY + 1.0D, entity.posZ);
		}
	}

}
