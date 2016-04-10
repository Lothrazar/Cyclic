package com.lothrazar.cyclicmagic.gui.slot;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWand extends Slot {

	public SlotWand(IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {

		// only blocks
		return !(itemstack.getItem() instanceof ItemCyclicWand) && Block.getBlockFromItem(itemstack.getItem()) != null;
	}

	@Override
	public void onSlotChanged() {

		if (this.getHasStack() && this.getStack().stackSize == 0) {
			this.putStack((ItemStack) null);
		}

		super.onSlotChanged();
	}
}
