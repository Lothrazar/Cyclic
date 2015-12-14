package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseWand extends Item{

	protected EnumParticleTypes sparkle = EnumParticleTypes.REDSTONE;
	protected String sound = null;
	//TODO: functions to set these, or maybe a constroctur particle input?
	public BaseWand(int uses){
		this.setCreativeTab(ModMain.tabSamsContent);
		this.setMaxStackSize(1);
		this.setMaxDamage(uses);
	}
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		
		super.addInformation(stack, playerIn, tooltip, advanced);
    }
	public void addRecipe(){
		//some have no recipe
	}

	public void onSuccess(EntityPlayer playerIn,ItemStack stack,BlockPos pos){
		stack.damageItem(1, playerIn);
		playerIn.swingItem();
		
		if(sparkle != null){
			UtilParticle.spawnParticle(playerIn.worldObj,sparkle, pos.getX(), pos.getY(), pos.getZ());
		}
		if(sound != null){
			playerIn.worldObj.playSoundAtEntity(playerIn, sound, 1.0F, 1.0F);
		}
	}
	
	public void onSuccess(EntityPlayer playerIn,ItemStack stack){
		this.onSuccess(playerIn, stack,playerIn.getPosition());
	}

	
	
	
	//TODO: these mods are just examples of what we MIGHT use.
	
	/**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ); 
    } 
    /**
     * Called each tick while using an item.
     * @param stack The Item being used
     * @param player The Player using the item
     * @param count The amount of time in tick the item has been used for continuously
     */
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
    	super.onUsingTick(stack, player, count);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }
    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
    	return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn); 
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
    }
}
