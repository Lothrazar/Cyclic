package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
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

	//http://minecraft.gamepedia.com/Furnace
	final static int SLOT_INPUT = 0;
	final static int SLOT_FUEL = 1;
	final static int SLOT_OUTPUT = 2;

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
	
		TileEntity tile = worldObj.getTileEntity(pos);
		 
		if(tile instanceof TileEntityFurnace){
			System.out.println("furnace");
			TileEntityFurnace furnace = (TileEntityFurnace)tile;
 
			
			if(held == null){
				System.out.println("try empty output slot");
				extractFurnaceOutput(furnace);
			}
			else if(isFuel(held)){
				System.out.println("is valid for FUEL");
				tryMergeStackIntoSlot(furnace,entityPlayer,held,SLOT_FUEL);
			}
			else if(canBeSmelted(held)){
				System.out.println("is valid for top");

				tryMergeStackIntoSlot(furnace,entityPlayer,held,SLOT_INPUT);
			}
			
			//nope does not satisfy this
			//boolean inf = furnace instanceof IItemHandlerModifiable;
		}
	}
	

	private void tryMergeStackIntoSlot(TileEntityFurnace furnace, EntityPlayer entityPlayer,ItemStack held, int slot){

		System.out.println("tryMergeStackIntoSlot:"+slot);
		ItemStack current = furnace.getStackInSlot(slot);
	
		if(current == null){
			//just done
			furnace.setInventorySlotContents(slot, held.copy());
			
			System.out.println("dump all");
			held = null;
			entityPlayer.inventory.setInventorySlotContents(slot, null);
		}
		else{
			
			UtilInventory.mergeItemsBetweenStacks(held, current);

			System.out.println("mergeItemsBetweenStacks");
			if(held == null || held.stackSize == 0){
				System.out.println("set null after merge");
				entityPlayer.inventory.setInventorySlotContents(slot, null);
			}
		}
		

		entityPlayer.inventory.markDirty();

	}
	
	private void extractFurnaceOutput(TileEntityFurnace furnace){

		ItemStack current = furnace.removeStackFromSlot(SLOT_OUTPUT); 
		if(current != null){
			UtilEntity.dropItemStackInWorld(furnace.getWorld(), furnace.getPos(), current);
		}
	}

	
	private boolean canBeSmelted(ItemStack input){
		//we literally get the smelt recipe instance to test if it has one
		ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(input);
        return (itemstack != null);
	}

	private boolean isFuel(ItemStack input){
		//how long does it burn for? zero means it isnt fuel
        return TileEntityFurnace.getItemBurnTime(input) > 0;
	}

}
