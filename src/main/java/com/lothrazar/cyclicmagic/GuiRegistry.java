package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.gui.ContainerItem;
import com.lothrazar.cyclicmagic.gui.GuiItemInventory;
import com.lothrazar.cyclicmagic.gui.InventoryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiRegistry implements IGuiHandler {

	//with thanks from https://github.com/coolAlias/Forge_Tutorials/blob/master/InventoryItemTutorial.java
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	
		if(ID == Const.GUI_INDEX){
			
			return new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItem()));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	
		if (ID == Const.GUI_INDEX)
		{
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiItemInventory((ContainerItem) new ContainerItem(player, player.inventory, new InventoryItem(player.getHeldItem())));
			
		}
		return null;
	}

}
