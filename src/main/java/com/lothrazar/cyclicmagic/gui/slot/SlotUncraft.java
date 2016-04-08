package com.lothrazar.cyclicmagic.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUncraft extends Slot {
	public SlotUncraft(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	/*
	 * public static boolean checkValid(ItemStack stack) { //used both from tile
	 * entity AND from here //if(stack !=
	 * null)System.out.println("SLOT "+stack.getItem().getUnlocalizedName());
	 * if(stack == null){return false;}
	 * 
	 * boolean isBlacklisted = (
	 * ModConfig.blacklistInput.contains(stack.getItem().getUnlocalizedName()));
	 * 
	 * if(isBlacklisted) return false; else{ if(ModConfig.blockIfCannotDoit ==
	 * false) return true; else{ //so we DO want to block if no recipe
	 * 
	 * UtilUncraft util = new UtilUncraft(stack); return util.canUncraft(); } }
	 * }
	 */

	@Override
	public boolean isItemValid(ItemStack stack) {
		return true;// SlotUncraft.checkValid(stack);
	}
}
