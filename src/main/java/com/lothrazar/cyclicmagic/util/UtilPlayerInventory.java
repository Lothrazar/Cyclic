package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UtilPlayerInventory 
{
	final static int width = 9;
	public static void shiftSlotDown(EntityPlayer player, int currentItem) 
	{
		int topNumber = currentItem + width;
		int midNumber = topNumber + width;
		int lowNumber = midNumber + width;
		//so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26, 35
		 
		ItemStack bar = player.inventory.getStackInSlot(currentItem);
		ItemStack top = player.inventory.getStackInSlot(topNumber);
		ItemStack mid = player.inventory.getStackInSlot(midNumber);
		ItemStack low = player.inventory.getStackInSlot(lowNumber);
  
		player.inventory.setInventorySlotContents(currentItem, null);
		player.inventory.setInventorySlotContents(currentItem, top);//lot so 0 gets what 9 had

		player.inventory.setInventorySlotContents(topNumber, null);
		player.inventory.setInventorySlotContents(topNumber, mid);

		player.inventory.setInventorySlotContents(midNumber, null);
		player.inventory.setInventorySlotContents(midNumber, low);
		
		player.inventory.setInventorySlotContents(lowNumber, null);
		player.inventory.setInventorySlotContents(lowNumber, bar);
	}

	public static void shiftSlotUp(EntityPlayer player, int currentItem) 
	{
		//so we move each up by nine
		int topNumber = currentItem + width;
		int midNumber = topNumber + width;
		int lowNumber = midNumber + width;
		//so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26, 35
		 
		ItemStack bar = player.inventory.getStackInSlot(currentItem);
		ItemStack top = player.inventory.getStackInSlot(topNumber);
		ItemStack mid = player.inventory.getStackInSlot(midNumber);
		ItemStack low = player.inventory.getStackInSlot(lowNumber);
  
		player.inventory.setInventorySlotContents(currentItem, null);
		player.inventory.setInventorySlotContents(currentItem, low);//lot so 0 gets what 9 had
 
		player.inventory.setInventorySlotContents(lowNumber, null);
		player.inventory.setInventorySlotContents(lowNumber, mid);
 
		player.inventory.setInventorySlotContents(midNumber, null);
		player.inventory.setInventorySlotContents(midNumber, top);
 
		player.inventory.setInventorySlotContents(topNumber, null);
		player.inventory.setInventorySlotContents(topNumber, bar);
	}

	public static void shiftBarDown(EntityPlayer player)
	{
		UtilPlayerInventory.shiftSlotDown(player, 0); 
		UtilPlayerInventory.shiftSlotDown(player, 1); 
		UtilPlayerInventory.shiftSlotDown(player, 2); 
		UtilPlayerInventory.shiftSlotDown(player, 3); 
		UtilPlayerInventory.shiftSlotDown(player, 4); 
		UtilPlayerInventory.shiftSlotDown(player, 5); 
		UtilPlayerInventory.shiftSlotDown(player, 6); 
		UtilPlayerInventory.shiftSlotDown(player, 7); 
		UtilPlayerInventory.shiftSlotDown(player, 8); 
	}

	public static void shiftBarUp(EntityPlayer player)
	{
		UtilPlayerInventory.shiftSlotUp(player, 0); 
		UtilPlayerInventory.shiftSlotUp(player, 1); 
		UtilPlayerInventory.shiftSlotUp(player, 2); 
		UtilPlayerInventory.shiftSlotUp(player, 3); 
		UtilPlayerInventory.shiftSlotUp(player, 4); 
		UtilPlayerInventory.shiftSlotUp(player, 5); 
		UtilPlayerInventory.shiftSlotUp(player, 6); 
		UtilPlayerInventory.shiftSlotUp(player, 7); 
		UtilPlayerInventory.shiftSlotUp(player, 8); 
	}
}
