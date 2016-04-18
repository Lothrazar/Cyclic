package com.lothrazar.cyclicmagic.gui.storage;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class InventoryStorage implements IInventory {

	public static final int	INV_SIZE	= 16;		
	private ItemStack[]			inventory	= new ItemStack[INV_SIZE];
	private final ItemStack	internalWand;

	private EntityPlayer		thePlayer;

	public EntityPlayer getPlayer() {

		return thePlayer;
	}

	public InventoryStorage(EntityPlayer player, ItemStack wand) {

		internalWand = wand;

		inventory = readFromNBT(internalWand);

		thePlayer = player;
	}

	@Override
	public String getName() {

		return "";
	}

	@Override
	public boolean hasCustomName() {

		return false;
	}

	@Override
	public ITextComponent getDisplayName() {

		return null;
	}

	@Override
	public int getSizeInventory() {

		return INV_SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int index) {

		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {

		ItemStack stack = getStackInSlot(slot);

		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				// Don't forget this line or your inventory will not be saved!

				markDirty();
			}
			else {
				// this method also calls onInventoryChanged, so we don't need
				// to call it again
				setInventorySlotContents(slot, null);
			}
		}

		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {

		// used to be 'getStackInSlotOnClosing'
		ItemStack stack = getStackInSlot(index);
		setInventorySlotContents(index, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {

		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {

		return 64;
	}

	@Override
	public void markDirty() {

		for (int i = 0; i < getSizeInventory(); ++i) {
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) {
				inventory[i] = null;
			}
		}

		// set any empty item stacks (red zeroes) to empty
		for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++) {

			if (thePlayer.inventory.getStackInSlot(i) != null && thePlayer.inventory.getStackInSlot(i).stackSize == 0) {

				thePlayer.inventory.setInventorySlotContents(i, null);
			}
		}

		writeToNBT(internalWand, inventory);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	/************** public static ******************/

	public static ItemStack[] readFromNBT(ItemStack stack) {

		ItemStack[] inv = new ItemStack[INV_SIZE];

		if (stack == null || (stack.getItem() instanceof ItemCyclicWand) == false) { return inv; }
 
		NBTTagList items = UtilNBT.getTagCompoundNotNull(stack).getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
 
		for (int i = 0; i < items.tagCount(); ++i) {
			// 1.7.2+ change to items.getCompoundTagAt(i)

			NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			if (slot >= 0 && slot < INV_SIZE) {
				inv[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}

		return inv;
	}

	public static void writeToNBT(ItemStack wandStack, ItemStack[] theInventory) {

		NBTTagCompound tagcompound = UtilNBT.getTagCompoundNotNull(wandStack);
		// Create a new NBT Tag List to store itemstacks as NBT Tags
		NBTTagList items = new NBTTagList();
		ItemStack stack;
		for (int i = 0; i < theInventory.length; ++i) {
			stack = theInventory[i];
			if (stack != null && stack.stackSize == 0) {
				stack = null;
			}

			if (stack != null) {
				// Make a new NBT Tag Compound to write the itemstack and slot
				// index to
				NBTTagCompound itemTags = new NBTTagCompound();
				itemTags.setInteger("Slot", i);
				// Writes the itemstack in slot(i) to the Tag Compound we just
				// made
				stack.writeToNBT(itemTags);

				// add the tag compound to our tag list
				items.appendTag(itemTags);
			}
		}
		// Add the TagList to the ItemStack's Tag Compound with the name
		// "ItemInventory"
		tagcompound.setTag("ItemInventory", items);
	}

	public static void decrementSlot(ItemStack wand, int itemSlot) {

		ItemStack[] invv = InventoryStorage.readFromNBT(wand);
		invv[itemSlot].stackSize--;

		if (invv[itemSlot].stackSize == 0) {
			invv[itemSlot] = null;
		}
		InventoryStorage.writeToNBT(wand, invv);
	}

	public static ItemStack getFromSlot(ItemStack wand, int i) {

		if (i < 0 || i >= InventoryStorage.INV_SIZE) { return null; }
		return InventoryStorage.readFromNBT(wand)[i];
	}
 
	/******** required unmodified ****/

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public int getField(int id) {

		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {

		return 0;
	}

	@Override
	public void clear() {

	}
}
