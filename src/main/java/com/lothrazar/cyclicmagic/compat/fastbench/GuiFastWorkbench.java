package com.lothrazar.cyclicmagic.compat.fastbench;

import com.lothrazar.cyclicmagic.block.workbench.TileEntityWorkbench;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import shadows.fastbench.gui.GuiFastBench;

public class GuiFastWorkbench extends GuiFastBench {

	public GuiFastWorkbench(InventoryPlayer inv, World world, TileEntityWorkbench te) {
    super(inv, world, te.getPos());
		this.inventorySlots = new ClientContainerFastWorkbench(inv.player, world, te);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format("tile.block_workbench.name"), 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

}
