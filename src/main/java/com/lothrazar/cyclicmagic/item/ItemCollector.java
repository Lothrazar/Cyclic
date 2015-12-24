package com.lothrazar.cyclicmagic.item;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.util.Vector3;

public class ItemCollector extends Item {
	public ItemCollector() {
		super();
		this.setMaxStackSize(1);
	}

	//was SpellCollect
	private final static String NBT_MODE = "mode";
	private final static int MODE_ON = 1;
	private final static int MODE_OFF = 2;
	private final static int h_radius = 20;
	private final static int v_radius = 4;
	private final static float speed = 1.2F;

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		if (playerIn.isSneaking()) {
			if (worldIn.isRemote == false) {
				this.toggleMode(stack);
				// playerIn.addChatComponentMessage(new
				// ChatComponentText(this.getModeName(this.getMode(stack))));
			}
		}
		else {
			// is on

		}
		return stack;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (this.getMode(stack) == MODE_ON && worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0) {
			// tick once per second, if its turned on

			ItemCollector.trigger(worldIn, entityIn.getPosition());
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	private static void trigger(World world, BlockPos pos) {
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.fromBounds(x - h_radius, y - v_radius, z - h_radius, x + h_radius, y + v_radius, z + h_radius));

		int moved = 0;
		for (EntityItem eitem : found) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, speed);
			moved++;
		}

		List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.fromBounds(x - h_radius, y - v_radius, z - h_radius, x + h_radius, y + v_radius, z + h_radius));

		for (EntityXPOrb eitem : foundExp) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, speed);
			moved++;
		}

		if (moved > 0) {
			// do a particle or a durability?
		}
	}

	private int getMode(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (!stack.getTagCompound().hasKey(NBT_MODE)) {
			return MODE_OFF;// not set, dont return zero
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

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return (this.getMode(stack) == MODE_ON);
	}

	private void toggleMode(ItemStack stack) {
		int next = this.getMode(stack) == MODE_ON ? MODE_OFF : MODE_ON;

		this.setMode(stack, next);
	}
}
