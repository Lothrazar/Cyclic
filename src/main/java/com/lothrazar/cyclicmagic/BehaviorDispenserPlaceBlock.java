package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;


public class BehaviorDispenserPlaceBlock extends BehaviorDefaultDispenseItem{

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		//we want to place in front of the dispenser - which is based on where its facing
		BlockPos posForPlant = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
		BlockPos posSoil = posForPlant.down();

		if(stack != null && stack.getItem() instanceof ItemSeeds){
			
			ItemSeeds seed = (ItemSeeds)stack.getItem();
			
			/*dammit crops is private
			 * 
			 * public class ItemSeeds extends Item implements net.minecraftforge.common.IPlantable{
	    private Block crops;
	    //if that was public OR had a getter, we would just get and use that
	    */	
			

			Block crop = UtilReflection.getFirstPrivateBlock(seed);
			
			if(crop != null){
	    
				//mimic exactly what onItemUse.onItemUse is doing
				IBlockState state = world.getBlockState(posSoil);


		        boolean canSustainPlant = state.getBlock().canSustainPlant(state, world, posSoil, EnumFacing.UP, seed);
		  
		        if (canSustainPlant && world.isAirBlock(posForPlant))
		        {
		            world.setBlockState(posForPlant, crop.getDefaultState());
		            stack.stackSize--;


		            return stack;
		        }
			}
		}
		
		return super.dispenseStack(source, stack);
	}
}
