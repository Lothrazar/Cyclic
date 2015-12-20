package com.lothrazar.cyclicmagic.item;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPlayerLauncher extends Item {

	public static int DURABILITY = 200;

	public ItemPlayerLauncher() {
		super();
		this.setMaxDamage(DURABILITY);
		this.setMaxStackSize(1);
	}

	private final static String NBT_MODE = "mode";
	private final static int MODE_LAUNCH = 1;
	private final static int MODE_LOOK = 2;
	private final static int MODE_UP = 3;// only/always up
	private final static int MODE_HOVER = 4;

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		if (playerIn.isSneaking()) {
			if (worldIn.isRemote == false) {
				this.toggleMode(stack);
				playerIn.addChatComponentMessage(new ChatComponentText(this.getModeName(this.getMode(stack))));
			}
		}
		else {

			// thank you REF
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1435515-how-i-can-do-to-move-to-where-i-look
			// first reset falling/jumping
			playerIn.motionY = 0;
			playerIn.fallDistance = 0;

			float f = power();
			// double velX = playerIn.getLookVec().xCoord/2, velY = 0.7, velZ =
			// playerIn.getLookVec().xCoord/2;
			double velX = (double) (-MathHelper.sin(playerIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(playerIn.rotationPitch / 180.0F * (float) Math.PI) * f);
			double velZ = (double) (MathHelper.cos(playerIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(playerIn.rotationPitch / 180.0F * (float) Math.PI) * f);
			double velY = (double) (-MathHelper.sin((playerIn.rotationPitch) / 180.0F * (float) Math.PI) * f);

			switch (getMode(stack)) {
			case MODE_LAUNCH:
				// launch the player up and forward at minimum 30 degrees
				// regardless of look vector
				if (velY < 0) {
					velY *= -1;// first invert direction
				}
				if (velY < 0.4) {
					// if you are looking straight ahead, this is zero

					velY = 0.4 + playerIn.jumpMovementFactor;// do a bit of a
																// jump
				}
				break;
			case MODE_LOOK:
				// do nothing. leave y the same it is already following look vec
				break;
			case MODE_UP:
				velX = 0;
				velZ = 0;
				if (velY < 0) {
					velY *= -1;// make it always up never down
				}
				velY *= (power() / 2);
				break;
			case MODE_HOVER:
				velY = 0;// hover means do not go up or down
				break;
			}

			stack.damageItem(1, playerIn);
			playerIn.addVelocity(velX, velY, velZ);
		}

		return super.onItemRightClick(stack, worldIn, playerIn);
	}

	private String getModeName(int mode) {
		return StatCollector.translateToLocal("wand.launch.mode" + mode);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (this.getMode(stack) == MODE_HOVER) {
			entityIn.motionY = 0;// goal is to make player not fall
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(this.getModeName(this.getMode(stack)));
 
		int charge = stack.getMaxDamage() - stack.getItemDamage();//invese of damge
		tooltip.add(charge + "/" + stack.getMaxDamage());
		
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	private int getMode(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (!stack.getTagCompound().hasKey(NBT_MODE)) {
			return MODE_LAUNCH;// not set, dont return zero
		}
		else {
			return stack.getTagCompound().getInteger(NBT_MODE);
		}
	}

	private void setMode(ItemStack stack, int mode) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		stack.getTagCompound().setInteger(NBT_MODE, mode);
	}

	private void toggleMode(ItemStack stack) {
		int next = this.getMode(stack) + 1;

		if (next > MODE_HOVER) {
			next = MODE_LAUNCH;
		}// modulo increment

		this.setMode(stack, next);
	}

	private float power() {
		return 1.5F;// TODO: maybe tweak this one day from charging it or by
					// upgrades. maybe it goes down with durability
	}
}
