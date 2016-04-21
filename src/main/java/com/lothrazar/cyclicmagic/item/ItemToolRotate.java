package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolRotate extends Item implements IHasRecipe{

	
	public ItemToolRotate(){
		super();
	}
	
	@Override
	public void addRecipe() {
		// TODO Auto-generated method stub
		
	}
	
	@Override 
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
  {

		if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) { return super.onItemUse(stack, p, world, pos, hand, side, hitX, hitY, hitZ); }

		World worldObj = p.worldObj;
		IBlockState clicked = worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) { return super.onItemUse(stack, p, world, pos, hand, side, hitX, hitY, hitZ); }

		boolean isDone = UtilPlaceBlocks.rotateBlockValidState(pos, worldObj, side, p);
		if (isDone) {
		//	this.playSound(worldObj, clickedBlock, pos);
			//this.spawnParticle(worldObj, p, pos);
		}
		
    return super.onItemUse(stack, p, world, pos, hand, side, hitX, hitY, hitZ);//EnumActionResult.PASS;
  }
}
