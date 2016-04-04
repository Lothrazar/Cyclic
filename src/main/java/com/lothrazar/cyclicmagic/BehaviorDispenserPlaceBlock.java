package com.lothrazar.cyclicmagic;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;


public class BehaviorDispenserPlaceBlock extends BehaviorDefaultDispenseItem{

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		BlockPos pos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
		
		Block plantedBlock = null;
		//todo bet block from seed
		//Items.wheat_seeds ;
		
		if(stack != null && stack.getItem() instanceof ItemSeeds){
		/*dammit crops is private
		 * 
		 * public class ItemSeeds extends Item implements net.minecraftforge.common.IPlantable
{
    private Block crops;
    */	
			
			stack.getItem().onItemUse(stack, null, world, pos, null, BlockDispenser.getFacing(source.getBlockMetadata()), 0, 0, 0);
			/*net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, stack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
        {
            worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
            --stack.stackSize;
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }*/
		}
		
		
		if(plantedBlock != null && plantedBlock.canPlaceBlockAt(world, pos)) {
			world.setBlockState(pos, plantedBlock.getDefaultState());
			stack.stackSize--;
			return stack;
		}

		return super.dispenseStack(source, stack);
	}
}
