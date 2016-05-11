package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UtilInventory {


	public static ItemStack getPlayerItemIfHeld(EntityPlayer player) {

		ItemStack wand = player.getHeldItemMainhand();
		if(wand == null){
			wand = player.getHeldItemOffhand();
		}
		return wand;
	}
	
	final static int width = 9;

	public static void mergeItemsBetweenStacks(ItemStack takeFrom, ItemStack moveTo) {

		int room = moveTo.getMaxStackSize() - moveTo.stackSize;

		if (room > 0) {
			int moveover = Math.min(takeFrom.stackSize, room);

			moveTo.stackSize += moveover;

			takeFrom.stackSize -= moveover;
		}
	}

	public static void shiftSlotDown(EntityPlayer player, int currentItem) {

		int topNumber = currentItem + width;
		int midNumber = topNumber + width;
		int lowNumber = midNumber + width;
		// so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26,
		// 35

		ItemStack bar = player.inventory.getStackInSlot(currentItem);
		ItemStack top = player.inventory.getStackInSlot(topNumber);
		ItemStack mid = player.inventory.getStackInSlot(midNumber);
		ItemStack low = player.inventory.getStackInSlot(lowNumber);

		player.inventory.setInventorySlotContents(currentItem, null);
		player.inventory.setInventorySlotContents(currentItem, top);// lot so 0 gets
		                                                            // what 9 had

		player.inventory.setInventorySlotContents(topNumber, null);
		player.inventory.setInventorySlotContents(topNumber, mid);

		player.inventory.setInventorySlotContents(midNumber, null);
		player.inventory.setInventorySlotContents(midNumber, low);

		player.inventory.setInventorySlotContents(lowNumber, null);
		player.inventory.setInventorySlotContents(lowNumber, bar);
	}

	public static void shiftSlotUp(EntityPlayer player, int currentItem) {

		// so we move each up by nine
		int topNumber = currentItem + width;
		int midNumber = topNumber + width;
		int lowNumber = midNumber + width;
		// so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26,
		// 35

		ItemStack bar = player.inventory.getStackInSlot(currentItem);
		ItemStack top = player.inventory.getStackInSlot(topNumber);
		ItemStack mid = player.inventory.getStackInSlot(midNumber);
		ItemStack low = player.inventory.getStackInSlot(lowNumber);

		player.inventory.setInventorySlotContents(currentItem, null);
		player.inventory.setInventorySlotContents(currentItem, low);// lot so 0 gets
		                                                            // what 9 had

		player.inventory.setInventorySlotContents(lowNumber, null);
		player.inventory.setInventorySlotContents(lowNumber, mid);

		player.inventory.setInventorySlotContents(midNumber, null);
		player.inventory.setInventorySlotContents(midNumber, top);

		player.inventory.setInventorySlotContents(topNumber, null);
		player.inventory.setInventorySlotContents(topNumber, bar);
	}

	public static void shiftBarDown(EntityPlayer player) {

		shiftSlotDown(player, 0);
		shiftSlotDown(player, 1);
		shiftSlotDown(player, 2);
		shiftSlotDown(player, 3);
		shiftSlotDown(player, 4);
		shiftSlotDown(player, 5);
		shiftSlotDown(player, 6);
		shiftSlotDown(player, 7);
		shiftSlotDown(player, 8);
	}

	public static void shiftBarUp(EntityPlayer player) {

		shiftSlotUp(player, 0);
		shiftSlotUp(player, 1);
		shiftSlotUp(player, 2);
		shiftSlotUp(player, 3);
		shiftSlotUp(player, 4);
		shiftSlotUp(player, 5);
		shiftSlotUp(player, 6);
		shiftSlotUp(player, 7);
		shiftSlotUp(player, 8);
	}

	public static void sortFromPlayerToInventory(World world, IInventory chest, EntityPlayer player) {

		// source:
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84

		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;

		// player inventory and the small chest have the same dimensions

		int START_CHEST = 0;
		int END_CHEST = chest.getSizeInventory();

		// inventory and chest has 9 rows by 3 columns, never changes. same as
		// 64 max stack size
		for (int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++) {
			chestItem = chest.getStackInSlot(islotChest);

			if (chestItem == null) {
				continue;
			}// empty chest slot

			for (int islotInv = 2 * Const.HOTBAR_SIZE; islotInv < player.inventory.getSizeInventory(); islotInv++) {

				invItem = player.inventory.getStackInSlot(islotInv);

				if (invItem == null) {
					continue;
				}// empty inventory slot

				if (invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() == chestItem.getItemDamage()) {
					// same item, including damage (block state)

					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
					room = chestMax - chestItem.stackSize;

					if (room <= 0) {
						continue;
					} // no room, check the next spot

					// so if i have 30 room, and 28 items, i deposit 28.
					// or if i have 30 room and 38 items, i deposit 30
					toDeposit = Math.min(invItem.stackSize, room);

					chestItem.stackSize += toDeposit;
					chest.setInventorySlotContents(islotChest, chestItem);

					invItem.stackSize -= toDeposit;

					if (invItem.stackSize <= 0) {
						// item stacks with zero count do not destroy
						// themselves, they show up and have unexpected behavior
						// in game so set to empty
						player.inventory.setInventorySlotContents(islotInv, null);
					}
					else {
						// set to new quantity
						player.inventory.setInventorySlotContents(islotInv, invItem);
					}
				}// end if items match
			}// close loop on player inventory items
		}// close loop on chest items
	}

	public static void decrStackSize(EntityPlayer entityPlayer, int currentItem) {

		if (entityPlayer.capabilities.isCreativeMode == false) {
			entityPlayer.inventory.decrStackSize(currentItem, 1);
		}
	}

}
