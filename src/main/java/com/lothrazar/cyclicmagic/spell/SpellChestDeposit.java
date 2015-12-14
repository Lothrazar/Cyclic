package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellChestDeposit extends BaseSpellExp
{   	 
	@Override
	public String getSpellName() 
	{
		return "deposit";
	} 
	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos)
	{
		if(super.canPlayerCast(world, player, pos) == false) {return false;}
	
		TileEntity tile = world.getTileEntity(pos);
		return (tile != null && tile instanceof TileEntityChest);
	}
	
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos) 
	{ 
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile != null && tile instanceof TileEntityChest)//redundant check, assuming the canPlayerCast was tested
		{
			TileEntityChest chest = (TileEntityChest)tile;
			
			sortFromPlayerToChestEntity(world, chest, player); 
			
			TileEntityChest cdouble = getChestAdj(chest);
			
			if(cdouble != null)
			{
				sortFromPlayerToChestEntity(world, cdouble, player); 
			}
		}
	}
 
	private final ResourceLocation icon = new ResourceLocation(Const.MODID,"textures/spells/spell_dummy_deposit.png");
	
	@Override
	public ResourceLocation getIconDisplay()
	{
		return icon;
	}
	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, "random.chestopen");

		super.onCastSuccess(world, player, pos);
	}
	 
	public class PlayerInventory
	{
		//100 to 103 is the armor
		public static final int ROWS = 4;//3 means ignore hotbar, 4 is include hotbar: TODO maybe toggle btw two in config one day?
		public static final int COLS = 9;
		public static final int SIZE = ROWS*COLS;
		public static final int START = 0;//top left=9; 1=hotbar
		public static final int END = START + SIZE;
	}
  
  	public void sortFromPlayerToChestEntity(World world, TileEntityChest chest, EntityPlayer entityPlayer)
  	{ 
  		//int totalItemsMoved = 0; 
  		int totalSlotsFreed = 0;
  		 
		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;
		
		//player inventory and the small chest have the same dimensions 
		
		int START_CHEST = 0; 
		int END_CHEST =  START_CHEST + 3*9; 
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++)
		{ 
			chestItem = chest.getStackInSlot(islotChest);
		
			if(chestItem == null)
			{  
				continue;
			}//not an error; empty chest slot
			 
			for(int islotInv = PlayerInventory.START; islotInv < PlayerInventory.END; islotInv++)
  			{ 
				invItem = entityPlayer.inventory.getStackInSlot(islotInv);
				
				if(invItem == null) 
				{ 
					continue;
			    }//empty inventory slot
		 
  				if( invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() ==  chestItem.getItemDamage() )
  				{  
  					//same item, including damage (block state)
  					
  					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
  					room = chestMax - chestItem.stackSize;
  					 
  					if(room <= 0) {continue;} // no room, check the next spot
  			 
  					//so if i have 30 room, and 28 items, i deposit 28.
  					//or if i have 30 room and 38 items, i deposit 30
  					toDeposit = Math.min(invItem.stackSize,room);
 
  					chestItem.stackSize += toDeposit;
  					chest.setInventorySlotContents(islotChest, chestItem);

  					invItem.stackSize -= toDeposit;

  					//totalItemsMoved += toDeposit;
  					//totalTypesMoved++;
  					
  					if(invItem.stackSize <= 0)//because of calculations above, should not be below zero
  					{
  						//item stacks with zero count do not destroy themselves, they show up and have unexpected behavior in game so set to empty
  						entityPlayer.inventory.setInventorySlotContents(islotInv,null); 
  						
  						totalSlotsFreed++;
  					}
  					else
  					{
  						//set to new quantity
  	  					entityPlayer.inventory.setInventorySlotContents(islotInv, invItem); 
  					} 
  				}//end if items match   
  			}//close loop on player inventory items 
		}//close loop on chest items
 
		if( totalSlotsFreed > 0) 
		{ 
			 
		//particles dont work, this only happens on server side (remote==false always)
			//SamsUtilities.spawnParticle(world,EnumParticleTypes.SLIME,chest.getPos().up()); 
		}
  	}
  	
	public TileEntityChest getChestAdj(TileEntityChest chest) 
	{
		TileEntityChest teAdjacent = null;
		if(chest.adjacentChestXNeg != null)
  	  	{
  	  		teAdjacent = chest.adjacentChestXNeg; 
  	  	}
  		if(chest.adjacentChestXPos != null)
  	  	{
  	  		teAdjacent = chest.adjacentChestXPos; 
  	  	}
  		if(chest.adjacentChestZNeg != null)
  	  	{
  	  		teAdjacent = chest.adjacentChestZNeg ; 
  	  	}
  		if(chest.adjacentChestZPos != null)
  	  	{
  	  		teAdjacent = chest.adjacentChestZPos; 
  	  	}
		return teAdjacent;
	}
}
