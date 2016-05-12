package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseTool extends BaseItem{
	private static final int DURABILITY = 1000;
	
	public BaseTool(){
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(DURABILITY); 
	}
	
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		playerIn.swingArm(hand);
		UtilItem.damageItem(playerIn, stack);
        return EnumActionResult.PASS;
    }
}
