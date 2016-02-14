package com.lothrazar.cyclicmagic.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWand extends Container{

	final InventoryWand inventory;
	final int SQ = 18;
	final int pad = 8;
	final int hotbar = 9;

	public ContainerWand(EntityPlayer par1Player, InventoryPlayer playerInventory, InventoryWand invoWand){

		this.inventory = invoWand;

		// player invo. below is copied from ContainerHopper.class
		for(int j = 0; j < invoWand.getSizeInventory(); j++){
			this.addSlotToContainer(new SlotWand(invoWand, j, pad + j * SQ, 30));
		}

		int i = 51;
		for(int l = 0; l < 3; ++l){
			for(int k = 0; k < 9; ++k){
				this.addSlotToContainer(new Slot(playerInventory, k + l * hotbar + hotbar, pad + k * SQ, l * SQ + i));
			}
		}

		for(int k = 0; k < 9; ++k){
			this.addSlotToContainer(new Slot(playerInventory, k, pad + k * SQ, 58 + i));
		}
	}

	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player){

		// this will prevent the player from interacting with the item that
		// opened the inventory:
		if(slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()){
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn){

		return inventory.isUseableByPlayer(playerIn);
	}

	public static final int INV_START = InventoryWand.INV_SIZE, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1, HOTBAR_END = HOTBAR_START + 8;

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index){

		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if(index < INV_START){
				// try to place in player inventory / action bar
				if(!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)){
					inventory.markDirty();
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory
			// or armor slots
			else{

				if(index >= INV_START){
					// place in custom inventory
					if(!this.mergeItemStack(itemstack1, 0, INV_START, false)){
						inventory.markDirty();
						return null;
					}
				}

				if(index >= INV_START && index < HOTBAR_START){
					// place in action bar
					if(!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END + 1, false)){
						inventory.markDirty();
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if(index >= HOTBAR_START && index < HOTBAR_END + 1){
					if(!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)){
						inventory.markDirty();
						return null;
					}
				}
			}

			if(itemstack1.stackSize == 0){
				slot.putStack((ItemStack) null);
			}
			else{
				slot.onSlotChanged();
			}

			if(itemstack1.stackSize == itemstack.stackSize){
				inventory.markDirty();
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		inventory.markDirty();
		return itemstack;
	}
}
