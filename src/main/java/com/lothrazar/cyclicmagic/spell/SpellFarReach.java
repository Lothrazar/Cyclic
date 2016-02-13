package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageSpellReach;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SpellFarReach extends BaseSpell {

	public SpellFarReach(int id, String n) {
		super.init(id, n);
		this.cost = 5;
	}
	
	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		
	}
	
	int maxRange = 64;//TODO: config
	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

        if(!p.capabilities.allowEdit) {
        	return false;
        }
	
        
        
		if(world.isRemote){
			//only client side can call this method. mouseover does not exist on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);
			BlockPos offset = ModMain.proxy.getBlockMouseoverOffset(maxRange);
			
			if(mouseover != null && offset != null){
				ModMain.network.sendToServer(new MessageSpellReach(mouseover,offset));
				
				//start is pos, end is offset
				BlockPos start = p.getPosition();
				
				UtilParticle.spawnParticleBeam(world, EnumParticleTypes.SPELL_INSTANT, start, mouseover, 3);
			}
		}
		
		return false;
	}
	
	public void castFromServer(BlockPos posMouseover, BlockPos posOffset, EntityPlayer p) {
		ItemStack heldWand = p.getHeldItem();
		if(heldWand == null || heldWand.getItem() instanceof ItemCyclicWand == false){
			return;
		}
		
		int itemSlot = InventoryWand.getSlotByBuildType(heldWand,p.worldObj.getBlockState(posMouseover));
		ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
		ItemStack toPlace = InventoryWand.getFromSlot(heldWand,itemSlot);
		
		if(toPlace != null  
				&& toPlace.getItem() != null  
				&& Block.getBlockFromItem(toPlace.getItem()) != null){
			
			IBlockState state = Block.getBlockFromItem(toPlace.getItem()).getStateFromMeta(toPlace.getMetadata());
			
			if(state != null  ){
				
				//kind of workaround since server packet handles message, but we want actual processing done in the spell
				
			
				//this kind of should be in on success.........but it doesnt have the block/state access
				UtilSound.playSound(p.worldObj,posOffset, state.getBlock().stepSound.getPlaceSound());
				
				//since it never really returns TRue up a bove, not to the server, we mimic what that would do. again, a hack since
				//this is hte only packet spell
				
				SpellRegistry.caster.castSuccess(this, p.worldObj, p, posOffset);
				
				//p.worldObj.setBlockState(pos, state);
				
				if(placeStateSafe(p.worldObj,p,posOffset,state)){

					if(state.getBlock().stepSound != null && state.getBlock().stepSound.getBreakSound() != null){
						UtilSound.playSoundAt(p, state.getBlock().stepSound.getPlaceSound());
					}
					
					if(p.capabilities.isCreativeMode == false){
						invv[itemSlot].stackSize--;
						//player.inventoryContainer.detectAndSendChanges();
						InventoryWand.writeToNBT(heldWand, invv);
					}
				}
			}
		}
	}
	
	private boolean placeStateSafe(World world, EntityPlayer player,BlockPos placePos, IBlockState placeState){
		if(world.isAirBlock(placePos) == false){

			//if there is a block here, we might have to stop
			Block blockHere = world.getBlockState(placePos).getBlock();
			if(blockHere.isReplaceable(world, placePos) == false){
				//for example, torches, and the top half of a slab if you click in the empty space
				return false;
			}
			
			//ok its a soft block so try to break it first try to destroy it first
			//unless it is liquid, don't try to destroy liquid
			if(blockHere.getMaterial() != Material.water && blockHere.getMaterial() != Material.lava){
				boolean dropBlock = true;
				world.destroyBlock(placePos,dropBlock);
			}
		}
		
		//either it was air, or it wasnt and we broke it
		return world.setBlockState(placePos, placeState);
	}
}
