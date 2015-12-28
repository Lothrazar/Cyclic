package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellBuilder extends BaseSpell {

	public SpellBuilder(int id, String n) {
		super(id, n);
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(pos == null || side == null){
			return false;
		}
		if(world.getBlockState(pos) == null || world.getBlockState(pos).getBlock() == null){
			return false;
		}
		
		BlockPos placePos = pos.offset(side);
		
		IBlockState placeState = world.getBlockState(pos);

		//damn too bad this is private
		//int slot = player.inventory.getInventorySlotContainItem(Item.getItemFromBlock(placeState.getBlock()));
		if(player.inventory.hasItem(Item.getItemFromBlock(placeState.getBlock()))){
			
			
			if(world.setBlockState(placePos, placeState)){
				//if it worked
				player.inventory.consumeInventoryItem(Item.getItemFromBlock(placeState.getBlock()));
				return true;
			}	
		}
		
		return false;
	}
	
	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {
		
		//UtilSound.playSoundAt(player, UtilSound.toss);
		super.onCastSuccess(world, player, pos);
	}
}
