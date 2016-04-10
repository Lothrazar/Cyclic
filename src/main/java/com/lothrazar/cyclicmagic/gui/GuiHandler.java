package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int	GUI_INDEX_UNCRAFTING	= 0;
	public static final int	GUI_INDEX_WAND				= 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_INDEX_UNCRAFTING) {

			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityUncrafting) { return new ContainerUncrafting(player.inventory, (TileEntityUncrafting) tileEntity); }
		}
		if (ID == GUI_INDEX_WAND) {

		return new ContainerWand(player, player.inventory, new InventoryWand(player, UtilSpellCaster.getPlayerWandIfHeld(player))); }
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_INDEX_UNCRAFTING) {

			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityUncrafting) { return new GuiUncrafting(player.inventory, (TileEntityUncrafting) tileEntity); }
		}
		if (ID == GUI_INDEX_WAND) {
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
			return new GuiWandInventory(new ContainerWand(player, player.inventory, new InventoryWand(player, wand)), wand);

		}
		return null;
	}
}
