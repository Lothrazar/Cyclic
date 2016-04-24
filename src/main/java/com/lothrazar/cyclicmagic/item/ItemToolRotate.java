package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolRotate extends BaseTool implements IHasRecipe {

	public ItemToolRotate() {
		super();
	}

	@Override
	public void addRecipe() {

	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World worldObj, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		if (pos == null || worldObj.getBlockState(pos) == null || side == null) {
			return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);
		}

		IBlockState clicked = worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) {
			//cancelled
			return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);
		}

		UtilPlaceBlocks.rotateBlockValidState(pos, worldObj, side, p);

		return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
	}
}
