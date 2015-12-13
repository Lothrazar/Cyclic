package com.lothrazar.samsmagic.item;

import java.util.List; 

import org.apache.logging.log4j.Level;

import com.lothrazar.samsmagic.ModMain; 

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemChestSack extends Item
{ 
	private static final String KEY_ITEMQTY = "itemqty";
	private static final String KEY_ITEMDMG = "itemdmg";
	private static final String KEY_ITEMIDS = "itemids";

	public ItemChestSack()
	{  
		super();  
		this.setMaxStackSize(1);
	}
   
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) 
	{
		if(itemStack.getTagCompound() == null) {itemStack.setTagCompound(new NBTTagCompound());}
		String count = itemStack.getTagCompound().getString("count"); 
		if(count == null ) {count = "0";}
        		 
        list.add("Items: " + EnumChatFormatting.GREEN +count);
 
        String stacks = itemStack.getTagCompound().getString("stacks"); 
        if(stacks == null) {stacks=  "0";}
        	          
        list.add("Stacks: " + EnumChatFormatting.GREEN +stacks);          
	}   
	  
	public static void sortFromSackToChestEntity(TileEntityChest chest, ItemStack held, PlayerInteractEvent event)
  	{
		if(held.getTagCompound() == null) {held.setTagCompound(new NBTTagCompound());}
	  
		int[] itemids = held.getTagCompound().getIntArray(KEY_ITEMIDS);
		int[] itemdmg = held.getTagCompound().getIntArray(KEY_ITEMDMG);
		int[] itemqty = held.getTagCompound().getIntArray(KEY_ITEMQTY);
		
		if(itemids == null){return;}
 
  		//int totalItemsMoved = 0; 
  		int totalSlotsFreed = 0;
  		
  		//boolean debug = false;
  	  	
		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;
		  
		int START_CHEST = 0;
		//int START_INV = 9;//because we are ignoring the item hotbar, we skip the first row this way
		//player inventory and the small chest have the same dimensions 
		int size = 3*9;
		int END_CHEST =  START_CHEST + size;
		//int END_INV = START_INV + size;

		int item;
		int meta;
		int qty;
 
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++)
		{
			chestItem = chest.getStackInSlot(islotChest);
		
			if(chestItem == null) { continue; } // empty chest slot
			 
			for(int i = 0; i < itemids.length ; i++)
			{
				item = itemids[i];
				if(item == 0){continue;}//empty inventory slot 
				
				meta = itemdmg[i];
				qty = itemqty[i];
				
				invItem = new ItemStack(Item.getItemById(item),qty,meta);
		  
  				if(invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() ==  chestItem.getItemDamage() )
  				{   
  					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
  					room = chestMax - chestItem.stackSize;
  					 
  					if(room <= 0) {continue;} // no room, check the next spot
  					 
  					//so if i have 30 room, and 28 items, i deposit 28.
  					//or if i have 30 room and 38 items, i deposit 30
  					toDeposit = Math.min(invItem.stackSize,room);

  					 //puttin stuffi n the c hest, ooh yeahhh
  					chestItem.stackSize += toDeposit;
  					chest.setInventorySlotContents(islotChest, chestItem);

  					invItem.stackSize -= toDeposit;

  					//totalItemsMoved += toDeposit;
  					 
  					if(invItem.stackSize <= 0)//because of calculations above, should not be below zero
  					{
  						//item stacks with zero count do not destroy themselves, they show up and have unexpected behavior in game so set to empty
 
  						itemids[i] = 0;
  						itemdmg[i] = 0;
  						itemqty[i] = 0;
  						
  						totalSlotsFreed++;
  					}
  					else
  					{
  						//set to new quantity in sack 
  						itemqty[i] = invItem.stackSize; 
  					}
  					   
  				}//end if items match   
  			}//close loop on player inventory items
		}//close loop on chest items
		
		if( totalSlotsFreed > 0 ) 
		{
			//String msg = "Sack Sort deposited " + totalItemsMoved + " items."; 
		 
			//dont do sound, there is already a sound played from hitting the block
			//event.entityPlayer.playSound("random.bowhit1",5, 5);
		}
	 
		event.entityPlayer.getCurrentEquippedItem().getTagCompound().setIntArray(KEY_ITEMIDS,itemids);
		event.entityPlayer.getCurrentEquippedItem().getTagCompound().setIntArray(KEY_ITEMDMG,itemdmg);
		event.entityPlayer.getCurrentEquippedItem().getTagCompound().setIntArray(KEY_ITEMQTY,itemqty);
  	}
	 
	public static void createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos)
	{
		int[] itemids = heldChestSack.getTagCompound().getIntArray("itemids");
		int[] itemdmg = heldChestSack.getTagCompound().getIntArray("itemdmg");
		int[] itemqty = heldChestSack.getTagCompound().getIntArray("itemqty");
		
		if(itemids==null)
		{
			ModMain.logger.log(Level.WARN, "null nbt problem in itemchestsack");
			return;
		}
 
		entityPlayer.worldObj.setBlockState(pos,  Blocks.chest.getDefaultState());
		
		TileEntityChest chest = (TileEntityChest)entityPlayer.worldObj.getTileEntity(pos);  
	
		int item;
		int meta;
		int qty;
		ItemStack chestItem;
		 
		for(int i = 0; i < itemids.length ; i++)
		{
			item = itemids[i];
			if(item == 0){continue;}//empty slot 
			
			meta = itemdmg[i];
			qty = itemqty[i];
			
			chestItem = new ItemStack(Item.getItemById(item),qty,meta);
	
			chest.setInventorySlotContents(i, chestItem); 
		}
		 
		entityPlayer.destroyCurrentEquippedItem();

		ModMain.playSoundAt(entityPlayer, "random.chestopen");
  	} 
}
