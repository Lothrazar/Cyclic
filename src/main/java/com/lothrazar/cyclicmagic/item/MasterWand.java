package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.SpellCaster;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterWand extends Item {

	private final int MAXCHARGE = 1000;

	public MasterWand() {
		this.setMaxStackSize(1);
		this.setMaxDamage(MAXCHARGE);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		int charge = stack.getMaxDamage() - stack.getItemDamage();//invese of damge
		tooltip.add(charge + "/" + MAXCHARGE);
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	/**
	 * Called each tick while using an item.
	 * 
	 * @param stack
	 *            The Item being used
	 * @param player
	 *            The Player using the item
	 * @param count
	 *            The amount of time in tick the item has been used for
	 *            continuously
	 */
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		System.out.println("on tick wand using");
		super.onUsingTick(stack, player, count);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		System.out.println("onItemRightClick");
		SpellCaster.tryCastCurrent(worldIn, playerIn, playerIn.getPosition(), null);
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		System.out.println("onItemUse");

		SpellCaster.tryCastCurrent(worldIn, playerIn, pos, side);

		return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger
	 * the "Use Item" statistic.
	 */
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps
	 * to check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// if held by something not a player? such as custom npc/zombie/etc
		if (entityIn instanceof EntityPlayer == false) {
			return;
		}
		EntityPlayer p = (EntityPlayer) entityIn;
		if (p.inventory.currentItem != itemSlot && worldIn.rand.nextDouble() > 0.1) {
		
			int curr = stack.getItemDamage();
			if(curr > 0){
				stack.setItemDamage(curr - 1);
			}
		}
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		// increasing damage makes it repair
		stack.setItemDamage(1); 
	}
}
