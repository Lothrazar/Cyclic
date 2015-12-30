package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class ItemChestSack extends Item {
	private static final String KEY_ITEMQTY = "itemqty";
	private static final String KEY_ITEMDMG = "itemdmg";
	private static final String KEY_ITEMIDS = "itemids";
	private static final String KEY_COUNT = "count";
	private static final String KEY_STACKS = "stacks";
	private static final String KEY_BLOCK = "block";
	private static final String KEY_BLOCKDISPLAY = "blockdisplay";

	public ItemChestSack() {
		super();
		this.setMaxStackSize(1);
		// imported from my old mod
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof IInventory) {

			sortToExisting((TileEntityChest) worldIn.getTileEntity(pos), stack);

		} else {
			BlockPos offset = pos.offset(side);

			if (worldIn.isAirBlock(offset) == false) {
				return false;
			}

			createAndFillChest(playerIn, stack, offset);
		}

		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean advanced) {
		if (itemStack.getTagCompound() == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		if( !itemStack.getTagCompound().hasKey(KEY_COUNT) || 
			!itemStack.getTagCompound().hasKey(KEY_STACKS)){
			return;//no info added
		}

		list.add(itemStack.getTagCompound().getString(KEY_BLOCKDISPLAY));
		String count = itemStack.getTagCompound().getInteger(KEY_COUNT)+"";
		String stacks = itemStack.getTagCompound().getInteger(KEY_STACKS)+"";
		if (count == "") {
			count = "0";
		}
		if (stacks == "") {
			stacks = "0";
		}

		list.add(EnumChatFormatting.AQUA + count + EnumChatFormatting.GREEN +" ("+ stacks +")");

		super.addInformation(itemStack, player, list, advanced);
	}

	public void sortToExisting(IInventory chest, ItemStack held) {
	
		if (held.getTagCompound() == null) {
			held.setTagCompound(new NBTTagCompound());
		}

		int[] itemids = held.getTagCompound().getIntArray(KEY_ITEMIDS);
		int[] itemdmg = held.getTagCompound().getIntArray(KEY_ITEMDMG);
		int[] itemqty = held.getTagCompound().getIntArray(KEY_ITEMQTY);

		if (itemids == null) {
			return;
		}

		// int totalItemsMoved = 0;
		int totalSlotsFreed = 0;

		// boolean debug = false;

		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;

		// int START_CHEST = 0;
		// int START_INV = 9;//because we are ignoring the item hotbar, we skip
		// the first row this way
		// player inventory and the small chest have the same dimensions
		// int size = 3*9;

		int item;
		int meta;
		int qty;

		// inventory and chest has 9 rows by 3 columns, never changes. same as
		// 64 max stack size
		for (int islotChest = 0; islotChest < chest.getSizeInventory(); islotChest++) {
 
			chestItem = chest.getStackInSlot(islotChest);

			if (chestItem == null) {
				continue;
			} // empty chest slot

			for (int i = 0; i < itemids.length; i++) {
				item = itemids[i];
				if (item == 0) {
					continue;
				}// empty inventory slot

				meta = itemdmg[i];
				qty = itemqty[i];

				invItem = new ItemStack(Item.getItemById(item), qty, meta);

				if (invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() == chestItem.getItemDamage()) {
					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
					room = chestMax - chestItem.stackSize;

					if (room <= 0) {
						continue;
					} // no room, check the next spot

					// so if i have 30 room, and 28 items, i deposit 28.
					// or if i have 30 room and 38 items, i deposit 30
					toDeposit = Math.min(invItem.stackSize, room);

					// puttin stuffi n the c hest, ooh yeahhh
					chestItem.stackSize += toDeposit;
					chest.setInventorySlotContents(islotChest, chestItem);

					invItem.stackSize -= toDeposit;

					// totalItemsMoved += toDeposit;

					if (invItem.stackSize <= 0)// because of calculations above,
												// should not be below zero
					{
						// item stacks with zero count do not destroy
						// themselves, they show up and have unexpected behavior
						// in game so set to empty

						itemids[i] = 0;
						itemdmg[i] = 0;
						itemqty[i] = 0;

						totalSlotsFreed++;
					} else {
						// set to new quantity in sack
						itemqty[i] = invItem.stackSize;
					}

				}// end if items match
			}// close loop on player inventory items
		}// close loop on chest items

		if (totalSlotsFreed > 0) {
			// String msg = "Sack Sort deposited " + totalItemsMoved +
			// " items.";

			// dont do sound, there is already a sound played from hitting the
			// block
			// event.entityPlayer.playSound("random.bowhit1",5, 5);
		}

		// event.entityPlayer.getCurrentEquippedItem()
		held.getTagCompound().setIntArray(KEY_ITEMIDS, itemids);
		held.getTagCompound().setIntArray(KEY_ITEMDMG, itemdmg);
		held.getTagCompound().setIntArray(KEY_ITEMQTY, itemqty);
	}

	public static void createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos) {
	
		int[] itemids = heldChestSack.getTagCompound().getIntArray(KEY_ITEMIDS);
		int[] itemdmg = heldChestSack.getTagCompound().getIntArray(KEY_ITEMDMG);
		int[] itemqty = heldChestSack.getTagCompound().getIntArray(KEY_ITEMQTY);

		if (itemids == null) {
			ModMain.logger.log(Level.WARN, "null nbt problem in itemchestsack");
			return;
		}

		if (Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)) == null) {

			ModMain.logger.log(Level.WARN, "Null block from id: " + heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
			return;
		}

		entityPlayer.worldObj.setBlockState(pos, Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)).getDefaultState());

		// is also a TileEntityChest
		IInventory invo = (IInventory) entityPlayer.worldObj.getTileEntity(pos);

		if (invo == null) {
			ModMain.logger.log(Level.WARN, "Null tile entity inventory, cannot fill from item stack");
			return;
		}

		int item;
		int meta;
		int qty;
		ItemStack chestItem;

		for (int i = 0; i < itemids.length; i++) {
			item = itemids[i];
			if (item == 0) {
				continue;
			}// empty slot

			meta = itemdmg[i];
			qty = itemqty[i];

			chestItem = new ItemStack(Item.getItemById(item), qty, meta);

			invo.setInventorySlotContents(i, chestItem);
		}

		// make the player slot empty
		entityPlayer.destroyCurrentEquippedItem();
		//playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
	}

	public static ItemStack createStackFromInventory(World world, EntityPlayer player, BlockPos posChest) {

		if (world.getTileEntity(posChest) == null || world.getTileEntity(posChest) instanceof IInventory == false) {
	 
			return null;
		}

		TileEntity tile = world.getTileEntity(posChest);
		IInventory invo = (IInventory) tile;

		ItemStack chestItem;
	
		ItemStack drop = new ItemStack(ItemRegistry.chest_sack, 1, 0);

		int stacks = 0;
		int count = 0;

		int[] itemids = new int[invo.getSizeInventory()];
		int[] itemqty = new int[invo.getSizeInventory()];
		int[] itemdmg = new int[invo.getSizeInventory()];

		// inventory and chest has 9 rows by 3 columns, never changes. same as
		// 64 max stack size
		for (int islotChest = 0; islotChest < invo.getSizeInventory(); islotChest++) {
			// zeroes to avoid nulls, and signify nothing goes there
			itemids[islotChest] = 0;
			itemqty[islotChest] = 0;
			itemids[islotChest] = 0;
			chestItem = invo.getStackInSlot(islotChest);

			if (chestItem == null) {
				continue;
			}// not an error; empty chest slot
			if (chestItem.getTagCompound() != null) {
				// probably has an enchantment
				player.dropPlayerItemWithRandomChoice(chestItem, false);
			} else {
				stacks++;
				count += chestItem.stackSize;

				itemids[islotChest] = Item.getIdFromItem(chestItem.getItem());
				itemdmg[islotChest] = chestItem.getItemDamage();
				itemqty[islotChest] = chestItem.stackSize;

			}
			// its either in the bag, or dropped on the player
			invo.setInventorySlotContents(islotChest, null);
		}

		if (drop.getTagCompound() == null){
			drop.setTagCompound(new NBTTagCompound());
		}
		drop.getTagCompound().setIntArray(KEY_ITEMIDS, itemids);
		drop.getTagCompound().setIntArray(KEY_ITEMDMG, itemdmg);
		drop.getTagCompound().setIntArray(KEY_ITEMQTY, itemqty);

		drop.getTagCompound().setInteger(KEY_COUNT, count);
		drop.getTagCompound().setInteger(KEY_STACKS, stacks);

		Block b = world.getBlockState(posChest).getBlock();
		drop.getTagCompound().setInteger(KEY_BLOCK, Block.getIdFromBlock(b));
		drop.getTagCompound().setString(KEY_BLOCKDISPLAY, b.getLocalizedName());

		return drop;
	}
}