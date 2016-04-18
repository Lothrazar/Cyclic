package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.storage.ContainerStorage;
import com.lothrazar.cyclicmagic.gui.storage.GuiStorage;
import com.lothrazar.cyclicmagic.gui.storage.InventoryStorage;
import com.lothrazar.cyclicmagic.gui.wand.ContainerWand;
import com.lothrazar.cyclicmagic.gui.wand.GuiWandInventory;
import com.lothrazar.cyclicmagic.gui.uncrafting.ContainerUncrafting;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiUncrafting;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public static final int	GUI_INDEX_UNCRAFTING	= 0;
	public static final int	GUI_INDEX_WAND				= 1;
	public static final int	GUI_INDEX_EXTENDED		= 2;
	public static final int	GUI_INDEX_STORAGE		= 3;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	 
		switch (ID) {
		case GUI_INDEX_EXTENDED:
			return new com.lothrazar.cyclicmagic.gui.player.ContainerPlayerExtended(player.inventory, !world.isRemote, player);

			
		case GUI_INDEX_WAND:
			ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
			return new ContainerWand(player, player.inventory, new InventoryWand(player, wand)); 
	 
		case GUI_INDEX_UNCRAFTING:
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityUncrafting) { return new ContainerUncrafting(player.inventory, (TileEntityUncrafting) tileEntity); }
	
			break;
		case GUI_INDEX_STORAGE:
			ItemStack s = UtilSpellCaster.getPlayerWandIfHeld(player);
			return new ContainerStorage(player, player.inventory, new InventoryStorage(player, s)); 
		
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	
		if (world instanceof WorldClient)
		switch (ID) {
		case GUI_INDEX_EXTENDED:
			return new GuiPlayerExtended(player);
			
		case GUI_INDEX_WAND:
			ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
			return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);

		case GUI_INDEX_UNCRAFTING:

			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityUncrafting) { return new GuiUncrafting(player.inventory, (TileEntityUncrafting) tileEntity); }
		
			break;
		case GUI_INDEX_STORAGE:
			ItemStack s = UtilSpellCaster.getPlayerWandIfHeld(player);
			return new GuiStorage(new ContainerStorage(player, player.inventory, new InventoryStorage(player, s)), s);

		}
		return null;
	}
}
