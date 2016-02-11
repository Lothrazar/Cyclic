package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageSpellReach;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellFarReach extends BaseSpell {

	public SpellFarReach(int id, String n) {
		super(id, n);
		this.cost = 5;
	}
	
	int maxRange = 64;//TODO: config
	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

        if(!p.capabilities.allowEdit) {
        	return false;
        }
	
		if(world.isRemote){
			//only client side can call this method. mouseover does not exist on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseover(maxRange);
			
			if(mouseover != null){
				ModMain.network.sendToServer(new MessageSpellReach(mouseover));
			}
		}
		
		return false;
	}
	
	public void castFromServer(BlockPos pos, EntityPlayer p) {
		ItemStack heldWand = p.getHeldItem();
		if(heldWand == null || heldWand.getItem() instanceof ItemCyclicWand == false){
			return;
		}
		
		int itemSlot = InventoryWand.getSlotByBuildType(p.getHeldItem());
		ItemStack[] invv = InventoryWand.readFromNBT(p.getHeldItem());
		ItemStack toPlace = InventoryWand.getFromSlot(p.getHeldItem(),itemSlot);
		
		if(toPlace != null  
				&& toPlace.getItem() != null  
				&& Block.getBlockFromItem(toPlace.getItem()) != null){
			
			IBlockState state = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
			
			if(state != null  ){
				
				//kind of workaround since server packet handles message, but we want actual processing done in the spell
				p.worldObj.setBlockState(pos, state);
			
				//this kind of should be in on success.........but it doesnt have the block/state access
				UtilSound.playSound(p.worldObj,pos, state.getBlock().stepSound.getPlaceSound());
				
				//since it never really returns TRue up a bove, not to the server, we mimic what that would do. again, a hack since
				//this is hte only packet spell
				
				SpellRegistry.caster.castSuccess(this, p.worldObj, p, pos);
				
				if(p.capabilities.isCreativeMode == false){
					invv[itemSlot].stackSize--;
					
					InventoryWand.writeToNBT(heldWand, invv);
				}
			}
		}
	}

}
