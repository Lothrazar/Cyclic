package com.lothrazar.cyclicmagic.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPlayerExtended extends Container {

	public InventoryPlayerExtended							inventory;
	/**
	 * Determines if inventory manipulation should be handled.
	 */
	public boolean															isLocalWorld;
	private final EntityPlayer									thePlayer;
	private static final EntityEquipmentSlot[]	ARMOR	= new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

	
	public ContainerPlayerExtended(InventoryPlayer playerInv, boolean par2, EntityPlayer player) {
		this.isLocalWorld = par2;
		this.thePlayer = player;
		inventory = new InventoryPlayerExtended(player);
		inventory.setEventHandler(this);
		if (!player.worldObj.isRemote) {
			inventory.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
		}

		for (int k = 0; k < 4; k++) {
			final EntityEquipmentSlot slot = ARMOR[k];
			this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + k * 18) {
				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					if (stack == null) {
						return false;
					}
					else {
						return stack.getItem().isValidArmor(stack, slot, thePlayer);
					}
				}

				@Override
				@SideOnly(Side.CLIENT)
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
				}
			});
		}

		int xPos,yPos;
		for (int i = 0; i < InventoryPlayerExtended.IROW; ++i) {
			for (int j = 0; j < InventoryPlayerExtended.ICOL; ++j) {
				
				xPos = 60 + j * SQ;
				yPos = 8 + i * SQ;
				
				this.addSlotToContainer(new Slot(inventory, j+i+1, xPos, yPos));
			}
		}

		

		for (int i = 0; i < VROW; ++i) {
			for (int j = 0; j < VCOL; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + (i + 1) * HOTBAR_SIZE, 8 + j * SQ, 84 + i * SQ));
			}
		}

		for (int i = 0; i < HOTBAR_SIZE; ++i) {
			this.addSlotToContainer(new Slot(playerInv, i, 8 + i * SQ, 142));
		}

		this.addSlotToContainer(new Slot(playerInv, SLOT_SHIELD, 97, 62) {
		
			@Override
			@SideOnly(Side.CLIENT)
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!player.worldObj.isRemote) {
			PlayerHandler.setPlayerBaubles(player, inventory);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	public static final int			SLOT_SHIELD			= 40;
	public static final int			SQ			= 18;
	public static final int			VROW			= 3;
	public static final int			VCOL			= 9;
	public static final int			HOTBAR_SIZE			= 9;
	/**
	 * Called when a player shift-clicks on a slot. You must override this or you
	 * will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int iSlot) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(iSlot);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (iSlot == 0) {
				if (!this.mergeItemStack(itemstack1, HOTBAR_SIZE + 4, 36+9 + 4, true)) { return null; }

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (iSlot >= 1 && iSlot < HOTBAR_SIZE) {
				if (!this.mergeItemStack(itemstack1, HOTBAR_SIZE + 4, 36+9 + 4, false)) { return null; }
			}
			else if (itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack1.getItem();
				int armorSlot = 8 - armor.armorType.getIndex();

				if (!this.mergeItemStack(itemstack1, armorSlot, armorSlot + 1, false)) { return null; }
			}
			else if (iSlot >= HOTBAR_SIZE + 4 && iSlot < 36 + 4) {
				if (!this.mergeItemStack(itemstack1, 36 + 4, 45 + 4, false)) { return null; }
			}
			else if (iSlot >= 36 + 4 && iSlot < 36+9 + 4) {
				if (!this.mergeItemStack(itemstack1, HOTBAR_SIZE + 4, 36 + 4, false)) { return null; }
			}
			else if (!this.mergeItemStack(itemstack1, HOTBAR_SIZE + 4, 45 + 4, false, slot)) { return null; }

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) { return null; }

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void putStacksInSlots(ItemStack[] s) {
		inventory.blockEvents = true;
		super.putStacksInSlots(s);
	}

	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, Slot ss) {
		boolean flag1 = false;
		int k = par2;

		if (par4) {
			k = par3 - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (par1ItemStack.isStackable()) {
			while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
				slot = (Slot) this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1)) {
					int l = itemstack1.stackSize + par1ItemStack.stackSize;
					if (l <= par1ItemStack.getMaxStackSize()) {
						// if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
						par1ItemStack.stackSize = 0;
						itemstack1.stackSize = l;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize()) {
						// if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
						par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = par1ItemStack.getMaxStackSize();
						slot.onSlotChanged();
						flag1 = true;
					}
				}

				if (par4) {
					--k;
				}
				else {
					++k;
				}
			}
		}

		if (par1ItemStack.stackSize > 0) {
			if (par4) {
				k = par3 - 1;
			}
			else {
				k = par2;
			}

			while (!par4 && k < par3 || par4 && k >= par2) {
				slot = (Slot) this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 == null) {
					// if (ss instanceof SlotBauble) unequipBauble(par1ItemStack);
					slot.putStack(par1ItemStack.copy());
					slot.onSlotChanged();
					par1ItemStack.stackSize = 0;
					flag1 = true;
					break;
				}

				if (par4) {
					--k;
				}
				else {
					++k;
				}
			}
		}
		return flag1;
	}
	/*
	 * @Override
	 * public boolean canMergeSlot(ItemStack par1ItemStack, Slot par2Slot)
	 * {
	 * return par2Slot.inventory != this.craftResult &&
	 * super.canMergeSlot(par1ItemStack, par2Slot);
	 * }
	 */
}
