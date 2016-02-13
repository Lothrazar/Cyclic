package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageSpellReplacer;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellReplacer extends BaseSpell {

	public SpellReplacer(int id, String n) {
		super(id, n);
		this.cooldown = 1;
	}

	int maxRange = 64;// TODO: config
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
/*// replacing these with mouseover...???//
		if (pos == null || side == null) {
			return false;
		}
		*/
        if(!player.capabilities.allowEdit) {
        	return false;
        }
        if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellReplacer(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}
		return false;
	}

	public boolean castFromServer(BlockPos posMouseover, EnumFacing side, EntityPlayer player) {

		World world = player.worldObj;
		ItemStack heldWand = player.getHeldItem();
		if(heldWand == null || heldWand.getItem() instanceof ItemCyclicWand == false){
			return false;
		}
		
		if (world.getBlockState(posMouseover) == null || world.getBlockState(posMouseover).getBlock() == null) {
			return false;
		}
		
		if(world.getTileEntity(posMouseover) != null){
			return false;//not chests, etc
		}
		
		IBlockState stateHere = world.getBlockState(posMouseover);
		Block blockHere = stateHere.getBlock();

		if(blockHere.getBlockHardness(world, posMouseover) == -1){
			return false; // is unbreakable-> like bedrock
		}
		
		int itemSlot = InventoryWand.getSlotByBuildType(heldWand,world.getBlockState(posMouseover));
		ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
		ItemStack toPlace = InventoryWand.getFromSlot(heldWand,itemSlot);

		//ItemStack toPlace = player.inventory.getStackInSlot(itemSlot);
		
		if(toPlace == null || toPlace.getItem() == null || Block.getBlockFromItem(toPlace.getItem()) == null){
			return false;
		}
		
		IBlockState placeState = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
		 
		if(placeState.getBlock() == blockHere && blockHere.getMetaFromState(stateHere) == toPlace.getMetadata()){
			
			return false;//dont replace cobblestone with cobblestone
		}
 
		if (world.destroyBlock(posMouseover, true) && world.setBlockState(posMouseover, placeState)) {

			//replacement worked!
			if(player.capabilities.isCreativeMode == false){
				//player.inventory.decrStackSize(itemSlot, 1);
				//player.inventoryContainer.detectAndSendChanges();
				
				invv[itemSlot].stackSize--;
				InventoryWand.writeToNBT(heldWand, invv);
			}
			
			if(placeState.getBlock().stepSound != null && placeState.getBlock().stepSound.getPlaceSound() != null){
				UtilSound.playSoundAt(player, placeState.getBlock().stepSound.getPlaceSound());
			}
			
			return true;
		}

		return false;
	}
}
