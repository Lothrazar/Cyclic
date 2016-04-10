package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.net.MessageSpellFromServer;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack; 
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeBuild extends BaseSpellRange implements ISpellFromServer{

	final static int max = 32;//max search range
	public static enum PlaceType {
		PLACE, UP, DOWN;
	}
	private PlaceType type;
	public SpellRangeBuild(int id, String n,PlaceType t){

		super.init(id, n);
		this.cost = 30;
		this.cooldown = 5;
		this.type = t;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand,BlockPos pos, EnumFacing side){

		if(world.isRemote){
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);
			BlockPos offset = ModMain.proxy.getBlockMouseoverOffset(maxRange);
			
			
			if(mouseover != null && offset != null){

				ModMain.network.sendToServer(new MessageSpellFromServer(mouseover, offset, this.getID()));
			}
		}

		return false;
	}

	public  void castFromServer(BlockPos posMouseover, BlockPos posOffset, EntityPlayer p){

		World world = p.worldObj;
		
		ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(p);
		
		if(heldWand == null){
			return;
		}

		int itemSlot = InventoryWand.getSlotByBuildType(heldWand, world.getBlockState(posMouseover));
		IBlockState state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

		if(state == null){
			return;
		}
		
		BlockPos posToPlaceAt = null;
	
		
		switch(type){
		case DOWN:
			//start at posMouseover, go DOWN until air
			BlockPos posLoop = posMouseover;
			for(int i = 0; i < max; i++){
				if(world.isAirBlock(posLoop)){
					posToPlaceAt = posLoop;
					break;
				}
				else{
					posLoop = posLoop.down();
				}
			}
			break;
		case PLACE:
			//use offset NOT mouseover
			posToPlaceAt = posOffset;
			break;
		case UP:
			//start at posMouseover, go up until air
			BlockPos pLoop = posMouseover;
			for(int i = 0; i < max; i++){
				if(world.isAirBlock(pLoop)){
					posToPlaceAt = pLoop;
					break;
				}
				else{
					pLoop = pLoop.up();
				}
			}
			break;
		default:
			break;
		}

		if(UtilPlaceBlocks.placeStateSafe(p.worldObj, p, posToPlaceAt, state)){

			UtilSpellCaster.castSuccess(this, p.worldObj, p, posOffset);
			
			if(state.getBlock().getStepSound() != null && state.getBlock().getStepSound().getBreakSound() != null){
				UtilSound.playSound(p, state.getBlock().getStepSound().getPlaceSound());
			}

			if(p.capabilities.isCreativeMode == false){
				
				InventoryWand.decrementSlot(heldWand,itemSlot);
				//ItemStack[] invv = InventoryWand.readFromNBT(heldWand);
				//invv[itemSlot].stackSize--;
				// player.inventoryContainer.detectAndSendChanges();
				//InventoryWand.writeToNBT(heldWand, invv);
			}

			// yes im spawning particles on the server side, but the
			// util handles that
			this.spawnParticle(p.worldObj, p, posMouseover);
			this.playSound(p.worldObj, state.getBlock(), posOffset);
		}
	}
}
