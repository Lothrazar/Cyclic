package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolPush  extends BaseTool implements IHasRecipe{

	@Override
	public void addRecipe() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		BlockPos resultPosition = UtilPlaceBlocks.pushBlock(worldObj, p, pos, side);
		/*
		Block newSpot = null;

		if (resultPosition != null && p.worldObj.getBlockState(resultPosition) != null) {
			newSpot = p.worldObj.getn            mnn                      nh nn nnnn  xxzzzzzzzzzzzzzzz					11BlockState(resultPosition).getBlock();
		}

		if (newSpot != null) {
			this.spawnParticle(p.worldObj, p, pos);
			this.playSound(p.worldObj, newSpot, pos);
		}
		*/
		return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
	}
}
