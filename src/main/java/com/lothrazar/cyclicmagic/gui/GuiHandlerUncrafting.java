package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.block.TileEntityUncrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerUncrafting implements IGuiHandler {

	public static final int guiId = 0;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityUncrafting) {
			return new ContainerUncrafting(player.inventory, (TileEntityUncrafting) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity instanceof TileEntityUncrafting) {
			return new GuiUncrafting(player.inventory, (TileEntityUncrafting) tileEntity);
		}
		return null;
	}
}
