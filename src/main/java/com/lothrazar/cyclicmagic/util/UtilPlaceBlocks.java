package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList; 
import java.util.ConcurrentModificationException;
import org.apache.logging.log4j.Level;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class UtilPlaceBlocks
{
	
	public static void circle(World world, EntityPlayer player, ItemStack heldWand,BlockPos pos) 
	{
		int diameter = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		// based on http://stackoverflow.com/questions/1022178/how-to-make-a-circle-on-a-grid
		//also http://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm
				
		int centerX = pos.getX();
		int centerZ = pos.getZ();
		
		int height = (int)pos.getY();
		
		int radius = diameter/2;
		
		int z = radius;
		int x = 0;
		int d = 2 - diameter;
		
		ArrayList<BlockPos> circleList = new ArrayList<BlockPos>(); 
		
		do 
		{
			circleList.add(new BlockPos(centerX + x, height, centerZ + z)); 
	        circleList.add(new BlockPos(centerX + x, height, centerZ - z));
	        circleList.add(new BlockPos(centerX - x, height, centerZ + z));
	        circleList.add(new BlockPos(centerX - x, height, centerZ - z));
	        circleList.add(new BlockPos(centerX + z, height, centerZ + x));
	        circleList.add(new BlockPos(centerX + z, height, centerZ - x));
	        circleList.add(new BlockPos(centerX - z, height, centerZ + x));
	        circleList.add(new BlockPos(centerX - z, height, centerZ - x));
	        
	        if (d < 0) 
	        {
	            d = d + (4 * x) + 6;
	        } 
	        else 
	        {
	            d = d + 4 * (x - z) + 10;
	            z--;
	        }
	        
	        x++;
	    } 
		while (x <= z);
		

		int itemSlot; 
		IBlockState state;
		for(BlockPos posCurrent : circleList)
		{
			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);
			
			placeWithSoundAndDecrement(world,player,heldWand,itemSlot,posCurrent,state);
		}
	}

	public static void square(World world, EntityPlayer player, ItemStack heldWand,BlockPos pos, int radius)
	{
		//search in a cube
		int xMin = pos.getX() - radius;
		int xMax = pos.getX() + radius; 
		int zMin = pos.getZ() - radius;
		int zMax = pos.getZ() + radius;

		int y = pos.getY();
		
		BlockPos posCurrent;

		int itemSlot; 
		IBlockState state;
		for (int x = xMin; x <= xMax; x++)
		{ 
			for (int z = zMin; z <= zMax; z++)
			{
				itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
				state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);
				
				posCurrent = new BlockPos(x, y, z);
				
				if(world.isAirBlock(posCurrent) == false){continue;}
			  
				placeWithSoundAndDecrement(world,player,heldWand,itemSlot,posCurrent,state);
				
			}  
		} //end of the outer loop
   
	}

	public static void stairway(World world, EntityPlayer player,ItemStack heldWand,BlockPos position)
	{ 
		int want = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		boolean isLookingUp = (player.getLookVec().yCoord >= 0);//TODO: use this somehow? to place up/down? 
    
		boolean goVert = true;	
	
		EnumFacing pfacing = UtilEntity.getPlayerFacing(player);

        //it starts at eye level, so do down and forward one first
		BlockPos posCurrent = player.getPosition().down().offset(pfacing);
		

		int itemSlot; 
		IBlockState state;
		for(int i = 1; i < want + 1; i++)
		{

			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

			
			if(goVert)
			{
				if(isLookingUp)
					posCurrent = posCurrent.up();
				else
					posCurrent = posCurrent.down();
			}
			else{
				posCurrent = posCurrent.offset(pfacing);
			}
			goVert = (i % 2 == 0);//alternate between going forward and going vertical
			
			placeWithSoundAndDecrement(world,player, heldWand,itemSlot,posCurrent,state);
		}
	}
	
	public static void line(World world, EntityPlayer player,ItemStack heldWand, BlockPos pos,EnumFacing efacing)
	{
		int want = ItemCyclicWand.BuildType.getBuildSize(heldWand);
		int skip = 1;
		
		BlockPos posCurrent;

		int itemSlot; 
		IBlockState state;
		for(int i = 1; i < want + 1; i = i + skip)
		{ 
			posCurrent = pos.offset(efacing, i);

			itemSlot = InventoryWand.getSlotByBuildType(heldWand, null);
			state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);


			if(state == null){
				return;//then inventory is completely empty
			}
			
			placeWithSoundAndDecrement(world,player,heldWand,itemSlot,posCurrent,state);
			
		}
	}

	//from command place blocks
	private static boolean placeWithSoundAndDecrement(World world,EntityPlayer player, ItemStack heldWand,int itemSlot,BlockPos posCurrent,  IBlockState placing)
	{
		boolean success = placeStateSafe(world,player,posCurrent,placing);

		if(success){

			UtilSound.playSound(player, placing.getBlock().getStepSound().getPlaceSound());

			if(player.capabilities.isCreativeMode == false )
			{
				//player.inventory.decrStackSize(player.inventory.currentItem, 1);
				InventoryWand.decrementSlot(heldWand,itemSlot);
			}
		}
		
		return success;
	}
	
	
	
	//from spell range build
	public static boolean placeStateSafe(World world, EntityPlayer player, BlockPos placePos, IBlockState placeState){
		if(placePos == null){
			return false;
		}
		
		if(world.isAirBlock(placePos) == false){

			// if there is a block here, we might have to stop
			IBlockState stateHere = world.getBlockState(placePos);
			
			if(stateHere != null){
				
				Block blockHere = stateHere.getBlock();
				
				if(blockHere.isReplaceable(world, placePos) == false){
					// for example, torches, and the top half of a slab if you click
					// in the empty space
					return false;
				}
	
				// ok its a soft block so try to break it first try to destroy it
				// unless it is liquid, don't try to destroy liquid
				if(blockHere.getMaterial(stateHere) != Material.water && blockHere.getMaterial(stateHere) != Material.lava){
					boolean dropBlock = true;
					world.destroyBlock(placePos, dropBlock);
				}
			}
		}
		
		boolean success = false;
		
		try{
			//as soon as i added the try catch, it started never (rarely) happening
			
			//we used to pass a flag as third argument, such as '2'
			//default is '3'
			success = world.setBlockState(placePos, placeState);
			
			//world.markBlockForUpdate(posMoveToHere);
		}
		catch(ConcurrentModificationException e){
			ModMain.logger.log(Level.WARN,"ConcurrentModificationException");
			ModMain.logger.log(Level.WARN,e.getMessage());//message is null??
			ModMain.logger.log(Level.WARN,e.getStackTrace().toString());
			success = false;
		}
		// either it was air, or it wasnt and we broke it
		return success;
	}
	
}
