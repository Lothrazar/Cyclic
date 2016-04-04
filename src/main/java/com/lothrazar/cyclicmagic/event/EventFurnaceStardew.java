package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;


public class EventFurnaceStardew{
	//inspired by stardew valley
	

	@SubscribeEvent
	public void onPlayerFurnace(PlayerInteractEvent event){
		
		

		EntityPlayer entityPlayer = event.getEntityPlayer();
		//ignore in creative// left clicking just breaks it anyway
		if(entityPlayer.capabilities.isCreativeMode){
			return;
		}
		
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if(pos == null){
			return;
		}
		ItemStack held = entityPlayer.getHeldItemMainhand();
		int slot = entityPlayer.inventory.currentItem;
		
		if(held == null){
			held = entityPlayer.getHeldItemOffhand();
			//offhand slot is the highest number
			slot = entityPlayer.inventory.getSizeInventory() - 1;
		}
		if(held == null){
			return;
			//empty it?
		}
		
		TileEntity tile = worldObj.getTileEntity(pos);
		 
		if(tile instanceof TileEntityFurnace){
			System.out.println("furnace");
			TileEntityFurnace furnace = (TileEntityFurnace)tile;

			//int burnTime = TileEntityFurnace.getItemBurnTime(held);
			
			
			/*
			//http://minecraft.gamepedia.com/Furnace
Slot 0: The item(s) being smelted.
Slot 1: The item(s) to use as the next fuel source.
Slot 2: The item(s) in the result slot.
			 * */
			
			
			/*
			 * ItemStack copy = stack.copy();
                    copy.stackSize += stackInSlot.stackSize;
                    getInv().setInventorySlotContents(slot, copy);

                    */
			if(furnace.isItemValidForSlot(0,held)){
				System.out.println("is valid for top");
				furnace.setInventorySlotContents(0, held);
				
				entityPlayer.inventory.setInventorySlotContents(slot, null);
				
				entityPlayer.inventory.markDirty();
			}
			if(furnace.isItemValidForSlot(1,held)){
				System.out.println("is valid for FUEL");
				furnace.setInventorySlotContents(1, held);
			}
			
			boolean inf = furnace instanceof IItemHandlerModifiable;
			
			System.out.println("IItemHandlerModifiable "+inf);

			
			
		}
	}
}
