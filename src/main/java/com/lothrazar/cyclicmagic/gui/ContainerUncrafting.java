package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.gui.slot.SlotUncraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerUncrafting extends Container {
	// tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
	
	public static final int					SLOTX_START				= 10;
	public static final int					SLOTY				= 28;
	//public static final int					SLOT				= 0;
	
	public static final int					SQ	= 18;
//	public static final int					SLOT_SECOND	= 1;
//	public static final int					SLOT_THIRD	= 2;
	protected TileEntityUncrafting	tileEntity;

	public ContainerUncrafting(InventoryPlayer inventoryPlayer, TileEntityUncrafting te) {
		tileEntity = te;

		//addSlotToContainer(new SlotUncraft(tileEntity, SLOT, SLOTX, SLOTY));
		//addSlotToContainer(new SlotUncraft(tileEntity, SLOT_SECOND, SLOTX, SLOTY + 18));
		//addSlotToContainer(new SlotUncraft(tileEntity, SLOT_THIRD, SLOTX, SLOTY + 18 + 18));

		for(int i = 0; i < tileEntity.getSizeInventory(); i++){

			addSlotToContainer(new SlotUncraft(tileEntity, i, SLOTX_START + i*SQ, SLOTY));
		}
		// commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);

	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			// merges the item into player inventory since its in the tileEntity
			if (slot < tileEntity.getSizeInventory()) {
				if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return null; }
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return null; }

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			}
			else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) { return null; }
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
