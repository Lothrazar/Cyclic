package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList; 
import java.util.ConcurrentModificationException;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class UtilPlaceBlocks
{
	
	public static void circle(World world, EntityPlayer player, BlockPos pos, IBlockState placing, int radius) 
	{
		// based on http://stackoverflow.com/questions/1022178/how-to-make-a-circle-on-a-grid
		//also http://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm
				
		int centerX = pos.getX();
		int centerZ = pos.getZ();
		
		int height = (int)pos.getY();
		
		int z = radius;
		int x = 0;
		int d = 2 - (2 * radius);
		
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
		
		for(BlockPos posCurrent : circleList)
		{
			if(world.isAirBlock(pos) == false){continue;}

			//but for the next 2 checks, halt if we run out of blocks/cost
			if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem() .stackSize == 0) {return;}
			

			//if(tryDrainExp(world,player,p) == false){break;}

			placeWithSoundAndDecrement(world,player,posCurrent,placing);
		}
	}

	public static void square(World world, EntityPlayer player, BlockPos pos, IBlockState placing, int radius)
	{
		//search in a cube
		int xMin = pos.getX() - radius;
		int xMax = pos.getX() + radius; 
		int zMin = pos.getZ() - radius;
		int zMax = pos.getZ() + radius;

		int y = pos.getY();
		
		BlockPos posCurrent;
		System.out.println("square start");

		//int numPlaced = 0;
		for (int x = xMin; x <= xMax; x++)
		{ 
			for (int z = zMin; z <= zMax; z++)
			{
				posCurrent = new BlockPos(x, y, z);
				
				if(world.isAirBlock(posCurrent) == false){continue;}
				System.out.println(x+" "+z);
				//but for the next 2 checks, halt if we run out of blocks/cost
				if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem() .stackSize == 0) {return;}
				
				//if(tryDrainExp(world,player,posCurrent) == false){break;}
	 
				placeWithSoundAndDecrement(world,player,posCurrent,placing);
				
			}  
		} //end of the outer loop
   
	}

	public static void stairway(World world, EntityPlayer player,BlockPos position, IBlockState placing, int want)
	{ 
		boolean isLookingUp = (player.getLookVec().yCoord >= 0);//TODO: use this somehow? to place up/down? 
    
		boolean goVert = true;	
	
		EnumFacing pfacing = UtilEntity.getPlayerFacing(player);

        //it starts at eye level, so do down and forward one first
		BlockPos posCurrent = player.getPosition().down().offset(pfacing);
		
		for(int i = 1; i < want + 1; i++)
		{
			if(goVert)
			{
				if(isLookingUp)
					posCurrent = posCurrent.up();
				else
					posCurrent = posCurrent.down();
			}
			else
				posCurrent = posCurrent.offset(pfacing);
			
			goVert = (i % 2 == 0);//alternate between going forward and going vertical
			
			if(world.isAirBlock(posCurrent) == false){continue;}
			//but for the next 2 checks, halt if we run out of blocks/cost
			if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem() .stackSize == 0) {return;}
	

			placeWithSoundAndDecrement(world,player,posCurrent,placing);
		}
	}
	
	public static void line(World world, EntityPlayer player,BlockPos pos, IBlockState placing, int want, int skip)
	{
      //  boolean isLookingUp = (player.getLookVec().yCoord >= 0);//TODO: use this somehow? to place up/down? 
        
		BlockPos posCurrent;
		EnumFacing efacing = (player.isSneaking()) ? EnumFacing.DOWN : UtilEntity.getPlayerFacing(player);
		
		for(int i = 1; i < want + 1; i = i + skip)
		{
			posCurrent = pos.offset(efacing, i);
			
			if(world.isAirBlock(posCurrent) == false){continue;}
			//but for the next 2 checks, halt if we run out of blocks/cost
			if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem() .stackSize == 0) {return;}

			//if(tryDrainExp(world,player,posCurrent) == false){break;}
			
			placeWithSoundAndDecrement(world,player,posCurrent,placing);
		}
	}

	//from command place blocks
	private static boolean placeWithSoundAndDecrement(World world,EntityPlayer player, BlockPos posCurrent,  IBlockState placing)
	{
		//world.setBlockState(posCurrent, placing);
		//changed to use safe
		boolean success = placeStateSafe(world,player,posCurrent,placing);

		if(success){
			
			UtilSound.playSound(player, placing.getBlock().getStepSound().getPlaceSound());
	
			
			if(player.capabilities.isCreativeMode == false)
			{
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
				//ModCommands.decrHeldStackSize(player);
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
			System.out.println("ConcurrentModificationException");
			System.out.println(e.getMessage());//message is null??
			System.out.println(e.getStackTrace().toString());
			success = false;
		}
		// either it was air, or it wasnt and we broke it
		return success;
	}
	
}
