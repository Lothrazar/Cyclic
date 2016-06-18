package com.lothrazar.cyclicmagic.gui.builder;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.gui.wand.ButtonBuildToggle;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiContainer {
	private TileEntityBuilder tile;
	public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityBuilder tileEntity) {
		super(new ContainerBuilder(inventoryPlayer, tileEntity));
		tile = tileEntity;
	}

	public GuiBuilder(Container c) {
		super(c);
	}

	final int		padding			= 4;
	@Override
	public void initGui() {

		super.initGui();

		int y = this.guiTop + padding;
		int x = this.guiLeft + 5;

		int width = 20;

		width = 50;
		this.buttonList.add(new ButtonBuilderType(tile, 2, x, y, width));
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	private static final String				folder		= "textures/gui/";
	private static final ResourceLocation	table		= new ResourceLocation(Const.MODID, folder + "table.png");
	private static final ResourceLocation	slot		= new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
	private static final ResourceLocation	progress	= new ResourceLocation(Const.MODID, folder + "progress.png");

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(table);
		int thisX = (this.width - this.xSize) / 2;
		int thisY = (this.height - this.ySize) / 2;
		int texture_width = 176;
		int texture_height = 166;
		int u = 0, v = 0;
		Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);

		this.mc.getTextureManager().bindTexture(slot);
		
		for(int k = 0; k < this.tile.getSizeInventory(); k++){
			
			Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 3 +k*Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);	
		}

		if (tile.getTimer() > 0 && tile.getStackInSlot(0) != null) {
			this.mc.getTextureManager().bindTexture(progress);

			float percent = ((float) tile.getTimer()) / ((float) TileEntityBuilder.TIMER_FULL);
			// maximum progress bar is 156, since the whole texture is 176 minus
			// 10 padding on each side
			int belowSlots = this.guiTop + 9 + 3 * Const.SQ;
			// Args: x, y, u, v, width, height, textureWidth, textureHeight
			Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots + 5, u, v, (int) (156 * percent), 7, 156, 7);
		}
	}
}
