package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotItemInv extends Slot{

	public SlotItemInv(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		// Everything returns true except an instance of our Item
		return !(itemstack.getItem() instanceof ItemCyclicWand);
	}
}
