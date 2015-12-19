package com.lothrazar.cyclicmagic.item;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterWand extends Item {

	private final int MAXCHARGE = 10000;//10k

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

	/*
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		System.out.println("on tick wand using");
		super.onUsingTick(stack, player, count);
	}
 
  * These do not fire on things like signs/noteblocks. anything that requires player to SHIFT to interact, well
  * these are cancelled without the shift
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
*/


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
		if (p.inventory.currentItem != itemSlot && worldIn.rand.nextDouble() > 0.95) {
		
			int curr = stack.getItemDamage();
			if(curr > 0){
				stack.setItemDamage(curr - 1);
			}
		}
	}

	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		//starts out damaged increasing damage makes it repair
		stack.setItemDamage(MAXCHARGE - 5); 
	}
}
