package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolHarvest extends BaseTool implements IHasRecipe{

	public static int			range_main		= 6;
	
	@Override
	public void addRecipe() {
		
		
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		UtilHarvestCrops.harvestArea(worldObj, p, pos, range_main);
		UtilHarvestCrops.harvestArea(worldObj, p, pos.up(), range_main);
		
		return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);
	}
}
