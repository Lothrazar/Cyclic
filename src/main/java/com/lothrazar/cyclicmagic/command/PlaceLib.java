package com.lothrazar.cyclicmagic.command;

import java.util.ArrayList; 
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class PlaceLib
{
	//library of functions/configs that apply to all /place[] commands
	//for all of these, we allow the player to be null
	
	//TODO: should xp cost be here as well?
	public static ArrayList<Block> allowed = new ArrayList<Block>();
	public static String allowedFromConfig = "";
	//public static int XP_COST_PER_PLACE;

	public static boolean canSenderPlace(ICommandSender sender)
	{
		EntityPlayer player = (EntityPlayer)sender;
		
		if(player == null){return false;}//was sent by command block or something, ignore it
		
		if(player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().stackSize == 0)
		{
			UtilChat.addChatMessage(player, "command.place.empty"); 
			return false;
		}
		Block pblock = Block.getBlockFromItem(player.inventory.getCurrentItem().getItem());

		if(pblock == null)
		{
			UtilChat.addChatMessage(player, "command.place.empty"); 
			return false;
		}
			
		if(PlaceLib.isAllowed(pblock) == false)
		{ 
			UtilChat.addChatMessage(player, "command.place.notallowed"); 
			return false;
		}
		
		return true;
	}
	
	public static void translateCSV()
	{
		//do this on the fly, could be items not around yet during config change
		System.out.println("placelib getBlockListFromCSV");
		/*
		if(PlaceLib.allowed.size() == 0)
			PlaceLib.allowed = ModCommands.getBlockListFromCSV(PlaceLib.allowedFromConfig); 
		*/
	}         

	public static boolean isAllowed(Block pblock)
	{
		translateCSV();
		
		return PlaceLib.allowed.size() == 0 || PlaceLib.allowed.contains(pblock);
	}
 
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

			placeSingle(world,player,posCurrent,placing);
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
	 
				placeSingle(world,player,posCurrent,placing);
				
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
	

			placeSingle(world,player,posCurrent,placing);
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
			
			placeSingle(world,player,posCurrent,placing);
		}
	}

	private static void placeSingle(World world,EntityPlayer player, BlockPos posCurrent,  IBlockState placing)
	{
		world.setBlockState(posCurrent, placing);

		UtilSound.playSound(player, placing.getBlock().getStepSound().getPlaceSound());
		
		if(player.capabilities.isCreativeMode == false)
		{
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
			//ModCommands.decrHeldStackSize(player);
		}
	}
}
