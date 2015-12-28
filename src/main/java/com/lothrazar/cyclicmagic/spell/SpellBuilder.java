package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellBuilder extends BaseSpell {

	public SpellBuilder(int id, String n) {
		super(id, n);
		this.cooldown = 1;
	}
	
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if (pos == null || side == null) {
			return false;
		}
        if(!player.capabilities.allowEdit) {
        	return false;
        }
		if (world.getBlockState(pos) == null || world.getBlockState(pos).getBlock() == null) {
			return false;
		}

		BlockPos placePos = pos.offset(side);

		IBlockState placeState = world.getBlockState(pos);

		int slotFound = -1;
		if(player.capabilities.isCreativeMode == false){
			//match using damage dropped, not exact meta value, so wood types line up but it ignores stair/log rotations
			int meta = placeState.getBlock().damageDropped(placeState);
			ItemStack compareStack = new ItemStack(placeState.getBlock(), 1, meta);
			ItemStack curr;
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				curr = player.inventory.getStackInSlot(i);
	
				if (curr != null && curr.isItemEqual(compareStack)) {
					slotFound = i;
					break;
				}
			}
			
			if(slotFound < 0 ){
				return false;
			}
		}

		if(world.isAirBlock(placePos) == false){

			//if there is a block here, we might have to stop
			Block blockHere = world.getBlockState(placePos).getBlock();
			if(blockHere.isReplaceable(world, placePos) == false){
				//for example, torches, and the top half of a slab if you click in the empty space
				return false;
			}
			
			//ok its a soft block so try to break it first try to destroy it first
			//unless it is liquid, don't try to destroy liquid
			if(blockHere.getMaterial() != Material.water && blockHere.getMaterial() != Material.lava){
				world.destroyBlock(placePos,true);//now this returns true/false but:
				//but if it fails to destroy, just continue on (eg: water)
			}
		}
		
		if (world.setBlockState(placePos, placeState)) {

			if(player.capabilities.isCreativeMode == false){
				player.inventory.decrStackSize(slotFound, 1);
				player.inventoryContainer.detectAndSendChanges();
			}
			
			if(placeState.getBlock().stepSound != null && placeState.getBlock().stepSound.getBreakSound() != null){
				UtilSound.playSoundAt(player, placeState.getBlock().stepSound.getPlaceSound());
			}
			
			return true;
		}

		return false;
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {

		//this is here to stop the default success sound from playing
	}
}
