package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerWand implements IGuiHandler{

	public static final int GUI_INDEX_WAND = 1;
	// with thanks from
	// https://github.com/coolAlias/Forge_Tutorials/blob/master/InventoryItemTutorial.java
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){

		if(ID == GUI_INDEX_WAND){

			return new ContainerWand(player, player.inventory, new InventoryWand(player, UtilSpellCaster.getPlayerWandIfHeld(player)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){

		if(ID == GUI_INDEX_WAND){
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiWandInventory((ContainerWand) new ContainerWand(player, player.inventory, new InventoryWand(player, UtilSpellCaster.getPlayerWandIfHeld(player))));

		}
		return null;
	}

}
