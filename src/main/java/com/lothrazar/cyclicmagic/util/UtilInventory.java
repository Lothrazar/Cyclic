package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack; 
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilInventory {

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

}
