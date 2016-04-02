package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.SpellCaster;
import com.lothrazar.cyclicmagic.gui.ContainerWand;
import com.lothrazar.cyclicmagic.gui.GuiWandInventory;
import com.lothrazar.cyclicmagic.gui.InventoryWand;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiRegistry implements IGuiHandler{

	// with thanks from
	// https://github.com/coolAlias/Forge_Tutorials/blob/master/InventoryItemTutorial.java
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){

		if(ID == Const.GUI_INDEX){

			return new ContainerWand(player, player.inventory, new InventoryWand(player, SpellCaster.getPlayerWandIfHeld(player)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){

		if(ID == Const.GUI_INDEX){
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiWandInventory((ContainerWand) new ContainerWand(player, player.inventory, new InventoryWand(player, SpellCaster.getPlayerWandIfHeld(player))));

		}
		return null;
	}

}
