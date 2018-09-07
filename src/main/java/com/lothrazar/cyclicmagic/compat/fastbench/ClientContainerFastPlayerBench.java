/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.compat.fastbench;

import javax.annotation.Nullable;

import com.lothrazar.cyclicmagic.playerupgrade.crafting.InventoryPlayerExtWorkbench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadows.fastbench.gui.ClientContainerFastBench;
import shadows.fastbench.gui.SlotCraftingSucks;

public class ClientContainerFastPlayerBench extends ClientContainerFastBench {

	private static final EntityEquipmentSlot[] ARMOR = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
	public static final int SLOT_SHIELD = 40;
	public static final int SQ = 18;
	public static final int VROW = 3;
	public static final int VCOL = 9;
	public static final int HOTBAR_SIZE = 9;
	final int pad = 8;
	EntityPlayer player;

	public ClientContainerFastPlayerBench(EntityPlayer player, World world) {
		super(player, world, 0, 0, 0);
		this.player = player;
		int xResult = 155, yResult = 22;
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();
		int xPos, yPos;

		this.addSlotToContainer(new SlotCraftingSucks(this, player, craftMatrix, craftResult, 0, xResult, yResult));

		for (int i = 0; i < InventoryPlayerExtWorkbench.IROW; ++i) {
			for (int j = 0; j < InventoryPlayerExtWorkbench.ICOL; ++j) {
				xPos = (j + 1) * SQ + 65;
				yPos = i * SQ + 6;
				this.addSlotToContainer(new Slot(craftMatrix, j + i * 3, xPos, yPos));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
		}

		for (int k = 0; k < ARMOR.length; k++) {
			final EntityEquipmentSlot slot = ARMOR[k];
			this.addSlotToContainer(new Slot(player.inventory, 36 + (3 - k), pad, pad + k * SQ) {

				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					return !stack.isEmpty() && stack.getItem().isValidArmor(stack, slot, player);
				}

				@Override
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
				}
			});
		}

		this.addSlotToContainer(new Slot(player.inventory, 40, 77, 62) {

			@Override
			public boolean isItemValid(@Nullable ItemStack stack) {
				return super.isItemValid(stack);
			}

			@Override
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, playerIn.world, playerIn);
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) return ItemStack.EMPTY;
				slot.onSlotChange(itemstack1, itemstack);

			} else if (index >= 10 && index < 37) {
				if (!(this.mergeItemStack(itemstack1, 46, 50, false) || this.mergeItemStack(itemstack1, 37, 46, false))) return ItemStack.EMPTY;

			} else if (index >= 37 && index < 46) {
				if (!(this.mergeItemStack(itemstack1, 46, 50, false) || this.mergeItemStack(itemstack1, 10, 37, false))) return ItemStack.EMPTY;

			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) return ItemStack.EMPTY;

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
