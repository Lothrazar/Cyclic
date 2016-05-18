package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.net.PacketSpellFromServer;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellRangeBuild extends BaseSpellRange implements ISpellFromServer {

	final static int max = 32;// max search range

	public static enum PlaceType {
		PLACE, UP, DOWN,LEFT,RIGHT;
	}

	private PlaceType type;

	public SpellRangeBuild(int id, String n, PlaceType t) {

		super.init(id, n);
		this.cost = 30;
		this.cooldown = 30;
		this.type = t;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);
			BlockPos offset = ModMain.proxy.getBlockMouseoverOffset(maxRange);

			if (mouseover != null && offset != null) {

				ModMain.network.sendToServer(new PacketSpellFromServer(mouseover, offset, this.getID()));

			}
		}

		return true;
	}

	public void castFromServer(BlockPos posMouseover, BlockPos posOffset, EntityPlayer p) {

		World world = p.worldObj;

		ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(p);

		if (heldWand == null) { return; }

		int itemSlot = InventoryWand.getSlotByBuildType(heldWand, world.getBlockState(posMouseover));
		IBlockState state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);

		if (state == null) { return; }

		BlockPos posToPlaceAt = null;
		EnumFacing facing = null;
		EnumFacing playerFacing = p.getHorizontalFacing();
		 
		switch (type) { 
		case DOWN:
			 facing = EnumFacing.DOWN;
			 break;
		case UP:
			 facing = EnumFacing.UP;
			 break;
		case LEFT:
			switch(playerFacing){
			case DOWN:
				break;
			case EAST:
				facing = EnumFacing.NORTH;
				break;
			case NORTH:
				facing = EnumFacing.WEST;
				break;
			case SOUTH:
				facing = EnumFacing.EAST;
				break;
			case UP:
				break;
			case WEST:
				facing = EnumFacing.SOUTH;
				break;
			default:
				break; 
			}
			
			break; 
		case RIGHT:
			switch(playerFacing){
			case DOWN:
				break;
			case EAST:
				facing = EnumFacing.SOUTH;
				break;
			case NORTH:
				facing = EnumFacing.EAST;
				break;
			case SOUTH:
				facing = EnumFacing.WEST;
				break;
			case UP:
				break;
			case WEST:
				facing = EnumFacing.NORTH;
				break;
			default:
				break; 
			} 
			break;
		case PLACE:
			break;
		default:
			break; 
		}
		
		if(facing == null){ 
			posToPlaceAt = posOffset;
		}
		else{
			BlockPos posLoop = posMouseover;
			for (int i = 0; i < max; i++) {
				if (world.isAirBlock(posLoop)) {
					posToPlaceAt = posLoop;
					break;
				}
				else {
					posLoop = posLoop.offset(facing);
				}
			}
		}

		if (UtilPlaceBlocks.placeStateSafe(p.worldObj, p, posToPlaceAt, state)) {

			UtilSpellCaster.castSuccess(this, p.worldObj, p, posOffset);

			//UtilSound.playSoundPlaceBlock(p, state.getBlock());
			

			if (p.capabilities.isCreativeMode == false) {

				InventoryWand.decrementSlot(heldWand, itemSlot); 
			}

			// yes im spawning particles on the server side, but the
			// util handles that

			this.spawnParticle(p.worldObj, p, posMouseover);
			
			Block newSpot = null;
			if (p.worldObj.getBlockState(posToPlaceAt) != null) {
				newSpot = p.worldObj.getBlockState(posToPlaceAt).getBlock();

				this.playSound(p.worldObj,p, newSpot, posToPlaceAt);
			} 
		}
	}
}
