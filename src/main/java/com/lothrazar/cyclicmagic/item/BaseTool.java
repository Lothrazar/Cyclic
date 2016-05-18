package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BaseTool extends BaseItem{
	
	public BaseTool(int durability){
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(durability); 
	}
	
	public void onUse(ItemStack stack, EntityPlayer playerIn, World worldIn, EnumHand hand){
		playerIn.swingArm(hand);
		UtilItem.damageItem(playerIn, stack); 
    }
}
