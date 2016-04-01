package com.lothrazar.cyclicmagic.item;


import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestSackEmpty extends Item{


	public ItemChestSackEmpty(){

		super();
		this.setMaxStackSize(64);
		
		this.setCreativeTab(CreativeTabs.tabInventory);
		// imported from my old mod
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
	}

    /**
     * Called when a Block is right-clicked with this Item
     */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(pos == null){
			return EnumActionResult.FAIL;
		}
		
		if(world.getTileEntity(pos) instanceof IInventory == false){
			return EnumActionResult.FAIL;
		}
		
		
		IInventory invo = (IInventory)world.getTileEntity(pos) ;
		
		NBTTagCompound itemTag = UtilNBT.writeInventoryToNewTag(invo, ItemChestSack.KEY_NBT);

		ItemStack drop = new ItemStack(ItemRegistry.chest_sack); // , 1, 0
		
		drop.setTagCompound(itemTag);
		
		drop.getTagCompound().setInteger(ItemChestSack.KEY_BLOCK, Block.getIdFromBlock(world.getBlockState(pos).getBlock()));
		
		entityPlayer.dropPlayerItemWithRandomChoice(drop, false);
		
		world.setBlockToAir(pos);
		
		stack.stackSize--;
		//entityPlayer.setHeldItem(hand, stack);
		
		return EnumActionResult.SUCCESS;
	}
	
	
	/*
	private boolean sortToExisting(EntityPlayer player, IInventory chest, ItemStack held){

		if(held.getTagCompound() == null){
			held.setTagCompound(new NBTTagCompound());
		}

		if(held.getTagCompound().hasKey(KEY_ITEMIDS) == false){
			return false;
		}
		int[] itemids = held.getTagCompound().getIntArray(KEY_ITEMIDS);
		int[] itemdmg = held.getTagCompound().getIntArray(KEY_ITEMDMG);
		int[] itemqty = held.getTagCompound().getIntArray(KEY_ITEMQTY);

		if(itemids == null){
			return false;
		}

		int totalItemsMoved = 0;

		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;

		int item;
		int meta;
		int qty;

		for(int islotChest = 0; islotChest < chest.getSizeInventory(); islotChest++){

			chestItem = chest.getStackInSlot(islotChest);

			if(chestItem == null){
				continue;
			} // empty chest slot

			for(int i = 0; i < itemids.length; i++){
				item = itemids[i];
				if(item == 0){
					continue;
				}// empty inventory slot

				meta = itemdmg[i];
				qty = itemqty[i];

				invItem = new ItemStack(Item.getItemById(item), qty, meta);

				if(invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() == chestItem.getItemDamage()){
					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
					room = chestMax - chestItem.stackSize;

					if(room <= 0){
						continue;
					} // no room, check the next spot

					// so if i have 30 room, and 28 items, i deposit 28.
					// or if i have 30 room and 38 items, i deposit 30
					toDeposit = Math.min(invItem.stackSize, room);

					chestItem.stackSize += toDeposit;
					chest.setInventorySlotContents(islotChest, chestItem);

					invItem.stackSize -= toDeposit;

					totalItemsMoved += toDeposit;

					if(invItem.stackSize <= 0){
						// item stacks with zero count do not destroy
						// themselves, they show up and have unexpected behavior
						// in game so set to empty

						itemids[i] = 0;
						itemdmg[i] = 0;
						itemqty[i] = 0;
					}
					else{
						// set to new quantity in sack
						itemqty[i] = invItem.stackSize;
					}

				}// end if items match
			}// close loop on player inventory items
		}// close loop on chest items

		held.getTagCompound().setIntArray(KEY_ITEMIDS, itemids);
		held.getTagCompound().setIntArray(KEY_ITEMDMG, itemdmg);
		held.getTagCompound().setIntArray(KEY_ITEMQTY, itemqty);

		return (totalItemsMoved > 0);
	}

	private boolean createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos){

		int[] itemids = heldChestSack.getTagCompound().getIntArray(KEY_ITEMIDS);
		int[] itemdmg = heldChestSack.getTagCompound().getIntArray(KEY_ITEMDMG);
		int[] itemqty = heldChestSack.getTagCompound().getIntArray(KEY_ITEMQTY);

		if(itemids == null){
			ModMain.logger.log(Level.WARN, "null nbt problem in itemchestsack");
			return false;
		}

		if(Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)) == null){

			ModMain.logger.log(Level.WARN, "Null block from id: " + heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
			return false;
		}

		entityPlayer.worldObj.setBlockState(pos, Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)).getDefaultState());

		// is also a TileEntityChest
		IInventory invo = (IInventory) entityPlayer.worldObj.getTileEntity(pos);

		if(invo == null){
			ModMain.logger.log(Level.WARN, "Null tile entity inventory, cannot fill from item stack");
			return false;
		}

		int item;
		int meta;
		int qty;
		ItemStack chestItem;

		for(int i = 0; i < itemids.length; i++){
			item = itemids[i];
			if(item == 0){
				continue;
			}// empty slot

			meta = itemdmg[i];
			qty = itemqty[i];

			chestItem = new ItemStack(Item.getItemById(item), qty, meta);

			invo.setInventorySlotContents(i, chestItem);
		}

		// make the player slot empty
		return true;
		// playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
	}

	public static ItemStack createStackFromInventory(World world, EntityPlayer player, BlockPos posChest){

//TODO: REDO ITEM STACKS TO NBT WITHOUT IDS
		if(world.getTileEntity(posChest) == null || world.getTileEntity(posChest) instanceof IInventory == false){

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
		for(int islotChest = 0; islotChest < invo.getSizeInventory(); islotChest++){
			// zeroes to avoid nulls, and signify nothing goes there
			itemids[islotChest] = 0;
			itemqty[islotChest] = 0;
			itemids[islotChest] = 0;
			chestItem = invo.getStackInSlot(islotChest);

			if(chestItem == null){
				continue;
			}// not an error; empty chest slot
			if(chestItem.getTagCompound() != null){
				// probably has an enchantment
				player.dropPlayerItemWithRandomChoice(chestItem, false);
			}
			else{
				stacks++;
				count += chestItem.stackSize;

				itemids[islotChest] = Item.getIdFromItem(chestItem.getItem());
				itemdmg[islotChest] = chestItem.getItemDamage();
				itemqty[islotChest] = chestItem.stackSize;

			}
			// its either in the bag, or dropped on the player
			invo.setInventorySlotContents(islotChest, null);
		}

		if(drop.getTagCompound() == null){
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
	}*/
}