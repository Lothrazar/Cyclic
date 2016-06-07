package com.lothrazar.cyclicmagic.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilEntity {

	public static void setMaxHealth(EntityLivingBase living, double max) {
		living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(max);
	}
	public static double getMaxHealth(EntityLivingBase living) {
		return living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
	}
	
	public static int incrementMaxHealth(EntityLivingBase living, int by) {
		int newVal = (int)getMaxHealth(living) + by;
		setMaxHealth(living,newVal);
		
		return newVal;
	}
	 
	public static EnumFacing getPlayerFacing(EntityLivingBase entity) {
		int yaw = (int) entity.rotationYaw;

		if (yaw < 0)              // due to the yaw running a -360 to positive 360
			yaw += 360;    // not sure why it's that way

		yaw += 22;     // centers coordinates you may want to drop this line
		yaw %= 360;  // and this one if you want a strict interpretation of the
		             // zones

		int facing = yaw / 45;   // 360degrees divided by 45 == 8 zones

		return EnumFacing.getHorizontal(facing / 2);
	}

	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block) {
		return dropItemStackInWorld(worldObj, pos, new ItemStack(block));
	}

	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item) {
		return dropItemStackInWorld(worldObj, pos, new ItemStack(item));
	}

	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
		EntityItem entityItem = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);

		if (worldObj.isRemote == false) { 
			// do not spawn a second 'ghost' one onclient side
			worldObj.spawnEntityInWorld(entityItem);
		}
		return entityItem;
	}

	public static double getSpeedTranslated(double speed) {

		return speed * 100;
	}

	public static double getJumpTranslated(double jump) {

		// double jump = horse.getHorseJumpStrength();
		// convert from scale factor to blocks
		double jumpHeight = 0;
		double gravity = 0.98;
		while (jump > 0) {
			jumpHeight += jump;
			jump -= 0.08;
			jump *= gravity;
		}
		return jumpHeight;
	}

}
