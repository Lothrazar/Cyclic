package com.lothrazar.cyclicmagic;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.IPlantable;

public class BehaviorPlantSeed extends BehaviorDefaultDispenseItem{

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack){

		World world = source.getWorld();
		// we want to place in front of the dispenser - which is based on where its facing
		BlockPos posForPlant = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
		BlockPos posSoil = posForPlant.down();

		if(stack != null && stack.getItem() instanceof IPlantable){

			IPlantable seed = (IPlantable) stack.getItem();

			IBlockState crop = seed.getPlant(world, posForPlant);

			if(crop != null){

				// mimic exactly what onItemUse.onItemUse is doing
				IBlockState state = world.getBlockState(posSoil);

				boolean canSustainPlant = state.getBlock().canSustainPlant(state, world, posSoil, EnumFacing.UP, seed);

				if(canSustainPlant ){
					
					if(world.isAirBlock(posForPlant)){
							
						world.setBlockState(posForPlant, crop);
						stack.stackSize--;

						return stack;
					}
					else{
						//System.out.println("can sustain plant yes, but not an air block. so do NOT drop on ground");
						return stack;//ie, dont do super
					}
				}
			}
		}

		return super.dispenseStack(source, stack);
	}
}
