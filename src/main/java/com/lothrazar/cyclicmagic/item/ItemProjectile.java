package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHomeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemProjectile extends Item {

	public static int DUNGEONRADIUS = 64;// TODO:CONFIG

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		onItemThrow(itemStackIn, worldIn, playerIn, hand);

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	private void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {

		if (held == null) { return; }

		if (held.getItem() == ItemRegistry.ender_dungeon) {

			BlockPos blockpos = UtilSearchWorld.findClosestBlock(player, Blocks.mob_spawner, DUNGEONRADIUS);

			if (blockpos != null) {

				EntityDungeonEye entityendereye = new EntityDungeonEye(world, player);

				doThrow(world, player, hand, entityendereye);

				entityendereye.moveTowards(blockpos);
			}
			else {
				// not found, so play sounds to alert player

				// also drop it on ground to signal a failed throw
				BlockPos pos = player.getPosition();
				if (player.capabilities.isCreativeMode == false) {
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
					
					UtilEntity.dropItemStackInWorld(world, pos, new ItemStack(ItemRegistry.ender_dungeon));
			
				}
				//fizz sound
				UtilSound.playSound(player,pos,SoundEvents.block_fire_extinguish);
			
			}
		}
		else if (held.getItem() == ItemRegistry.ender_tnt_1) {
			doThrow(world, player, hand, new EntityDynamite(world, player, 1));
		}
		else if (held.getItem() == ItemRegistry.ender_tnt_2) {
			doThrow(world, player, hand, new EntityDynamite(world, player, 2));
		}
		else if (held.getItem() == ItemRegistry.ender_tnt_4) {
			doThrow(world, player, hand, new EntityDynamite(world, player, 4));
		}
		else if (held.getItem() == ItemRegistry.ender_tnt_6) {
			doThrow(world, player, hand, new EntityDynamite(world, player, 6));
		}
		else if (held.getItem() == ItemRegistry.ender_blaze) {
			doThrow(world, player, hand, new EntityBlazeBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_bed) {
			doThrow(world, player, hand, new EntityHomeBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_torch) {
			doThrow(world, player, hand, new EntityTorchBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_wool) {
			doThrow(world, player, hand, new EntityShearingBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_fishing) {
			doThrow(world, player, hand, new EntityFishingBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_snow) {
			doThrow(world, player, hand, new EntitySnowballBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_water) {
			doThrow(world, player, hand, new EntityWaterBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_harvest) {
			doThrow(world, player, hand, new EntityHarvestBolt(world, player));
		}
		else if (held.getItem() == ItemRegistry.ender_lightning) {
			doThrow(world, player, hand, new EntityLightningballBolt(world, player));
		}
	}

	private static void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing) {

		if (!world.isRemote) {

			thing.func_184538_a(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntityInWorld(thing);
		}

		player.swingArm(hand);

		BlockPos pos = player.getPosition();

		UtilSound.playSound(player, pos, SoundEvents.entity_egg_throw, SoundCategory.PLAYERS);
	
		if (player.capabilities.isCreativeMode == false) {
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}
	}

	// backup in case function is renamed or removed -> ItemEnderPearl
	/*
	 * public void func_184538_a(Entity p_184538_1_, float p_184538_2_, float
	 * p_184538_3_, float p_184538_4_, float p_184538_5_, float p_184538_6_)
	 * {
	 * float f = -MathHelper.sin(p_184538_3_ * 0.017453292F) *
	 * MathHelper.cos(p_184538_2_ * 0.017453292F);
	 * float f1 = -MathHelper.sin((p_184538_2_ + p_184538_4_) * 0.017453292F);
	 * float f2 = MathHelper.cos(p_184538_3_ * 0.017453292F) *
	 * MathHelper.cos(p_184538_2_ * 0.017453292F);
	 * this.setThrowableHeading((double)f, (double)f1, (double)f2, p_184538_5_,
	 * p_184538_6_);
	 * this.motionX += p_184538_1_.motionX;
	 * this.motionZ += p_184538_1_.motionZ;
	 * 
	 * if (!p_184538_1_.onGround)
	 * {
	 * this.motionY += p_184538_1_.motionY;
	 * }
	 * }
	 */
}
