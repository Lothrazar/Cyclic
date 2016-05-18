package com.lothrazar.cyclicmagic.dispenser;

import com.lothrazar.cyclicmagic.util.UtilPlantable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorPlantSeed extends BehaviorDefaultDispenseItem {

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack){

		World world = source.getWorld();
		// we want to place in front of the dispenser 
		//which is based on where its facing
		
		BlockPos posForPlant = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
		 
		ItemStack returning = UtilPlantable.tryPlantSeed(world,posForPlant,stack);
		
		if(returning == null)
			return super.dispenseStack(source, stack);
		else
			return returning;
	}
}
