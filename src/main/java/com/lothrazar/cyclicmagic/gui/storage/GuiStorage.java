package com.lothrazar.cyclicmagic.gui.storage;

import org.lwjgl.opengl.GL11;

import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiStorage extends GuiContainer {

	private static final ResourceLocation	BACKGROUND	= new ResourceLocation(Const.MODID, "textures/gui/inventory_storage.png");

	static final int texture_width = 176;
	static final int texture_height = 212;

	public GuiStorage(ContainerStorage containerItem, ItemStack wand) {
		super(containerItem);
		this.xSize = texture_width;
		this.ySize = texture_height;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		int thisX = (this.width - this.xSize) / 2;
		int thisY = (this.height - this.ySize) / 2;
		int u = 0, v = 0;

		Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,  texture_width, texture_height, texture_width, texture_height);
	}
}
