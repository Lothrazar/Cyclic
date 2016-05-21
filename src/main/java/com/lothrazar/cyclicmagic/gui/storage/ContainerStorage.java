package com.lothrazar.cyclicmagic.gui.storage;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerStorage extends Container {

	final InventoryStorage	inventory;


	public static final int INV_START = InventoryStorage.INV_SIZE, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1, HOTBAR_END = HOTBAR_START + 8;

	final int						pad			= 8;
	final int						hotbar	= 9;

	public ContainerStorage(EntityPlayer par1Player, InventoryPlayer playerInventory, InventoryStorage invoWand) {

		this.inventory = invoWand;
		int x, y = pad,k,l,slot;
		//start the main container area
		for (l = 0; l < 6; ++l) {
			for (k = 0; k < 9; ++k) {
			x = pad + k * Const.SQ;
			y = pad + l * Const.SQ;
			slot = k + l * hotbar;
			this.addSlotToContainer(new Slot(invoWand, slot, x, y));
		}
	}

	 int yBase = pad+6* Const.SQ + 14;
	 //start the players inventory
		for (l = 0; l < 3; ++l) {
			for (k = 0; k < 9; ++k) {
				x = pad + k * Const.SQ;
				y = l * Const.SQ + yBase;
				slot = k + l * hotbar + hotbar;
				this.addSlotToContainer(new Slot(playerInventory, slot, x, y));
			}
		}

		//players hotbar
		int yhotbar = yBase + 3 * Const.SQ + pad/2;//y += SQ * 3 + 4;
		for ( k = 0; k < hotbar; ++k) {
			slot = k;
			x = pad + k * Const.SQ;
			this.addSlotToContainer(new Slot(playerInventory, slot, x, yhotbar));
		}
	}

/*
	@Override
  public Slot getSlot(int slotId)
  {
		int s = slotId;
		if(s > this.inventory.getSizeInventory()){
			System.out.println("Reduceby");
			s -= this.inventory.getSizeInventory();
		}
    return super.getSlot(s);
  }
*/

	//func_184996_a
	@Override
	public ItemStack slotClick(int slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {

		ItemStack wand = UtilInventory.getPlayerItemIfHeld(player);
		// this will prevent the player from interacting with the item that
		// opened the inventory:
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == wand) { return null; }
		return super.slotClick(slot, dragType, clickTypeIn, player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {

		return inventory.isUseableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {

		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if (index < INV_START) {
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) {
					inventory.markDirty();
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory
			// or armor slots
			else {

				if (index >= INV_START) {
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false)) {
						inventory.markDirty();
						return null;
					}
				}

				if (index >= INV_START && index < HOTBAR_START) {
					// place in action bar
					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END + 1, false)) {
						inventory.markDirty();
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if (index >= HOTBAR_START && index < HOTBAR_END + 1) {
					if (!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)) {
						inventory.markDirty();
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				inventory.markDirty();
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		inventory.markDirty();
		return itemstack;
	}
}
