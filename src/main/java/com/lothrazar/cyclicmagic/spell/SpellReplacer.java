package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
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

	public boolean castFromServer(BlockPos pos, EnumFacing side, EntityPlayer player) {

		World world = player.worldObj;

		if (world.getBlockState(pos) == null || world.getBlockState(pos).getBlock() == null) {
			return false;
		}
		
		if(world.getTileEntity(pos) != null){
			return false;//not chests, etc
		}
		
		IBlockState stateHere = world.getBlockState(pos);
		Block blockHere = stateHere.getBlock();

		if(blockHere.getBlockHardness(world, pos) == -1){
			return false; // is unbreakable-> like bedrock
		}
		
		int current = player.inventory.currentItem;
		if(current >= 9){
			return false;
		}
		int slotFound = current + 1;

		ItemStack toPlace = player.inventory.getStackInSlot(slotFound);
		
		if(toPlace == null || toPlace.getItem() == null || Block.getBlockFromItem(toPlace.getItem()) == null){
			return false;
		}
		
		IBlockState placeState = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
		 
		if(placeState.getBlock() == blockHere && blockHere.getMetaFromState(stateHere) == toPlace.getMetadata()){
			
			return false;//dont replace cobblestone with cobblestone
		}
 
		if (world.destroyBlock(pos, true) && world.setBlockState(pos, placeState)) {

			//replacement worked!
			if(player.capabilities.isCreativeMode == false){
				player.inventory.decrStackSize(slotFound, 1);
				player.inventoryContainer.detectAndSendChanges();
			}
			
			if(placeState.getBlock().stepSound != null && placeState.getBlock().stepSound.getPlaceSound() != null){
				UtilSound.playSoundAt(player, placeState.getBlock().stepSound.getPlaceSound());
			}
			
			return true;
		}

		return false;
	}
}
