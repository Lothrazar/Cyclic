package com.lothrazar.cyclicmagic.item;

import java.util.List;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemChestSack extends BaseItem implements IHasConfig {

	public static final String	name			= "chest_sack";

	public static final String	KEY_NBT		= "itemtags";
	public static final String	KEY_BLOCK	= "block";
 
	public ItemChestSack() {

		super();
		this.setMaxStackSize(1);
		// imported from my old mod
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		BlockPos offset = pos.offset(side);

		if (worldIn.isAirBlock(offset) == false) { return EnumActionResult.FAIL; }

		if (createAndFillChest(playerIn, stack, offset)) {
			playerIn.setHeldItem(hand, null);

			UtilSound.playSound(playerIn, pos, SoundRegistry.thunk);
			
			UtilEntity.dropItemStackInWorld(worldIn, playerIn.getPosition(), ItemRegistry.itemMap.get(ItemChestSackEmpty.name));
		}

		return EnumActionResult.SUCCESS;
	}

	private boolean createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos) {

		Block block = Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
		if (Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)) == null) {

			// ModMain.logger.log(Level.WARN, "Null block from id: " +
			// heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
			return false;
		}

		entityPlayer.worldObj.setBlockState(pos, block.getDefaultState());
		IInventory invo = (IInventory) entityPlayer.worldObj.getTileEntity(pos);
		if (invo == null) {
			// ModMain.logger.log(Level.WARN,
			// "Null tile entity inventory, cannot fill from item stack");
			return false;
		}

		UtilNBT.writeTagsToInventory(invo, heldChestSack.getTagCompound(), ItemChestSack.KEY_NBT);

		heldChestSack.stackSize = 0;
		heldChestSack.setTagCompound(null);

		return true;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean advanced) {

		int count = UtilNBT.countItemsFromNBT(itemStack.getTagCompound(), ItemChestSack.KEY_NBT);
		list.add("" + count);
	}

	@Override
	public void syncConfig(Configuration config) {

//		Property prop = config.get(Const.ConfigCategory.items, "ChestSack", true,
		//"A bag that transports chests along with its contents");
//		prop.setRequiresMcRestart(true);
//
//		ItemRegistry.setConfigMap(this,prop.getBoolean());
		
	}
}