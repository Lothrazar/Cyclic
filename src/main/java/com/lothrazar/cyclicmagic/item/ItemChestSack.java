package com.lothrazar.cyclicmagic.item;


import java.util.List;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class ItemChestSack extends Item{

	//TODO: also a different item ItemChestSackEmpty. or something. or a spell. w/e.
	public static final String KEY_NBT = "itemtags";
	public static final String KEY_BLOCK = "block";
	/*
	private static final String KEY_ITEMQTY = "itemqty";
	private static final String KEY_ITEMDMG = "itemdmg";
	private static final String KEY_ITEMIDS = "itemids";
	private static final String KEY_COUNT = "count";
	private static final String KEY_STACKS = "stacks";
	private static final String KEY_BLOCKDISPLAY = "blockdisplay";*/

	public ItemChestSack(){

		super();
		this.setMaxStackSize(1);
		// imported from my old mod
		// https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
	}

    /**
     * Called when a Block is right-clicked with this Item
     */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		/*
		if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof IInventory){

			if(sortToExisting(playerIn, (IInventory) worldIn.getTileEntity(pos), stack)){

				UtilSound.playSound(worldIn, pos, UtilSound.Own.thunk);
			}
		}
		else{
			*/
			BlockPos offset = pos.offset(side);

			if(worldIn.isAirBlock(offset) == false){
				return EnumActionResult.FAIL;
			}

			if(createAndFillChest(playerIn, stack, offset)){
				playerIn.setHeldItem(hand, null);

				UtilSound.playSound(worldIn, pos, UtilSound.Own.thunk);
			}

		//}

		return EnumActionResult.SUCCESS;
	}
	private boolean createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos){

		Block block = Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
		if(Block.getBlockById(heldChestSack.getTagCompound().getInteger(KEY_BLOCK)) == null){

		//	ModMain.logger.log(Level.WARN, "Null block from id: " + heldChestSack.getTagCompound().getInteger(KEY_BLOCK));
			return false;
		}
		
		entityPlayer.worldObj.setBlockState(pos, block.getDefaultState());
		IInventory invo = (IInventory) entityPlayer.worldObj.getTileEntity(pos);
		if(invo == null){
			//ModMain.logger.log(Level.WARN, "Null tile entity inventory, cannot fill from item stack");
			return false;
		}
		
		UtilNBT.writeTagsToInventory(invo, heldChestSack.getTagCompound(), ItemChestSack.KEY_NBT);
		
		heldChestSack.stackSize = 0;
		heldChestSack.setTagCompound(null);
		
		return true;
	}


	/*
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean advanced){

		if(itemStack.getTagCompound() == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}

		if(!itemStack.getTagCompound().hasKey(KEY_COUNT) || !itemStack.getTagCompound().hasKey(KEY_STACKS)){
			return;// no info added
		}

		list.add(itemStack.getTagCompound().getString(KEY_BLOCKDISPLAY));
		String count = itemStack.getTagCompound().getInteger(KEY_COUNT) + "";
		String stacks = itemStack.getTagCompound().getInteger(KEY_STACKS) + "";
		if(count == ""){
			count = "0";
		}
		if(stacks == ""){
			stacks = "0";
		}

		list.add(TextFormatting.AQUA + count + TextFormatting.GREEN + " (" + stacks + ")");

		
		
		super.addInformation(itemStack, player, list, advanced);
	}
	*/
	
}