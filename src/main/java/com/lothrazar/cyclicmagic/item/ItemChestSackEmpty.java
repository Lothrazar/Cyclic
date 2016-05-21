package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestSackEmpty extends BaseItem {

	public static final String name = "chest_sack_empty";

	public ItemChestSackEmpty() {

		super();
		this.setMaxStackSize(64);
		// imported from my old mod
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (pos == null) { return EnumActionResult.FAIL; }

		if (world.getTileEntity(pos) instanceof IInventory == false) { 
			
			if(world.isRemote){
				UtilChat.addChatMessage(entityPlayer, "item.chest_sack_empty.inventory");
			}
			
			return EnumActionResult.FAIL; 
		}

		IInventory invo = (IInventory) world.getTileEntity(pos);

		NBTTagCompound itemTag = UtilNBT.writeInventoryToNewTag(invo, ItemChestSack.KEY_NBT);

		ItemStack drop = new ItemStack(this);  

		drop.setTagCompound(itemTag);

		drop.getTagCompound().setInteger(ItemChestSack.KEY_BLOCK, Block.getIdFromBlock(world.getBlockState(pos).getBlock()));

		entityPlayer.dropItem(drop, false);

		world.setBlockToAir(pos);

		stack.stackSize--;
		
		UtilSound.playSound(entityPlayer,pos, SoundRegistry.thunk);
 
		return EnumActionResult.SUCCESS;
	}
}