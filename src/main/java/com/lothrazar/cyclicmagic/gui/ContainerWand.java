package com.lothrazar.cyclicmagic.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWand extends Container {
	final InventoryWand inventory;

	final int SQ = 18;

	public ContainerWand(EntityPlayer par1Player, InventoryPlayer playerInventory, InventoryWand invoWand) {
		this.inventory = invoWand;

		// player invo. below is copied from ContainerHopper.class
		for (int j = 0; j < invoWand.getSizeInventory(); j++) {
			this.addSlotToContainer(new Slot(invoWand, j, 44 + j * 18, 20));
		}

		int i = 51;
		for (int l = 0; l < 3; ++l) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + i));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 58 + i));
		}
	}

	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
		// this will prevent the player from interacting with the item that
		// opened the inventory:
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return inventory.isUseableByPlayer(playerIn);
	}

	private static final int INV_START = InventoryWand.INV_SIZE, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1, HOTBAR_END = HOTBAR_START + 8;

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
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory
			// or armor slots
			else {
				/*
				 * If your inventory only stores certain instances of Items, you
				 * can implement shift-clicking to your inventory like this:
				 * 
				 * // Check that the item is the right type if
				 * (itemstack1.getItem() instanceof ItemCustom) { // Try to
				 * merge into your custom inventory slots // We use
				 * 'InventoryItem.INV_SIZE' instead of INV_START just in case //
				 * you also add armor or other custom slots if
				 * (!this.mergeItemStack(itemstack1, 0, InventoryItem.INV_SIZE,
				 * false)) { return null; } } // If you added armor slots, check
				 * them here as well: // Item being shift-clicked is armor - try
				 * to put in armor slot if (itemstack1.getItem() instanceof
				 * ItemArmor) { int type = ((ItemArmor)
				 * itemstack1.getItem()).armorType; if
				 * (!this.mergeItemStack(itemstack1, ARMOR_START + type,
				 * ARMOR_START + type + 1, false)) { return null; } } Otherwise,
				 * you have basically 2 choices: 1. shift-clicking between
				 * player inventory and custom inventory 2. shift-clicking
				 * between action bar and inventory
				 * 
				 * Be sure to choose only ONE of the following
				 * implementations!!!
				 */
				/**
				 * Implementation number 1: Shift-click into your custom
				 * inventory
				 */
				if (index >= INV_START) {
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false)) {
						return null;
					}
				}

				/**
				 * Implementation number 2: Shift-click items between action bar
				 * and inventory
				 */
				// item is in player's inventory, but not in action bar
				if (index >= INV_START && index < HOTBAR_START) {
					// place in action bar
					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END + 1, false)) {
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if (index >= HOTBAR_START && index < HOTBAR_END + 1) {
					if (!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)) {
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
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;

	}
}
