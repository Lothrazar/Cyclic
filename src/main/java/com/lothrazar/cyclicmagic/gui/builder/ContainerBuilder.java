package com.lothrazar.cyclicmagic.gui.builder;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.gui.slot.SlotUncraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class ContainerBuilder extends Container {
	// tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
	
	public static final int					SLOTX_START	= 10;
	public static final int					SLOTY = 28;
	public static final int					SQ	= 18;
	protected TileEntityBuilder	tileEntity;

	public ContainerBuilder(InventoryPlayer inventoryPlayer, TileEntityBuilder te) {
		tileEntity = te;
		
		for(int i = 0; i < tileEntity.getSizeInventory(); i++){

			addSlotToContainer(new SlotUncraft(tileEntity, i, SLOTX_START + i*SQ, SLOTY));
		}
		// commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);

		this.detectAndSendChanges();
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			// merges the item into player inventory since its in the tileEntity
			if (slot < tileEntity.getSizeInventory()) {
				if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return null; }
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return null; }

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			}
			else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) { return null; }
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
    public void detectAndSendChanges() {
    	super.detectAndSendChanges();
    	for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = (IContainerListener)this.listeners.get(i);
            
            icontainerlistener.sendAllWindowProperties(this, this.tileEntity);
            //yes; the entityplayer is listening
//            if(icontainerlistener instanceof EntityPlayerMP){
//
//        		System.out.println("detectAndSendChanges EntityPlayerMP");
//            }
        }
    }

	@Override
    public void addListener(IContainerListener listener){
		//runs once when its opened
//		System.out.println("IContainerListener");
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileEntity);
    }
}
