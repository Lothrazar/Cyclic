package com.lothrazar.cyclicmagic.inventory;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonInventory extends GuiButton {

	private final int guiLeft;

	public GuiButtonInventory(int buttonId, int guiLeft, int guiTop, int x, int y, int width, int height, String buttonText) {
		super(buttonId, guiLeft + x, guiTop + y, width, height, buttonText);
		this.guiLeft = guiLeft;
	}
/*
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		int potionShift = getPotionShift(mc);
		return super.mousePressed(mc, mouseX - potionShift, mouseY);
	}
	*/
	
	
/*
	@Override
	public void drawButton(Minecraft mc, int xx, int yy) {
		if (this.visible) {
			int potionShift = getPotionShift(mc);

			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(GuiPlayerExtended.background);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = xx >= this.xPosition + potionShift && yy >= this.yPosition && xx < this.xPosition + this.width + potionShift && yy < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if (k == 1) {
				this.drawTexturedModalRect(this.xPosition + potionShift, this.yPosition, 200, 48, 10, 10);
			}
			else {
				this.drawTexturedModalRect(this.xPosition + potionShift, this.yPosition, 210, 48, 10, 10);
				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + 5 + potionShift, this.yPosition + this.height, 0xffffff);
			}

			this.mouseDragged(mc, xx, yy);

		}
	}*/
	
	
/*
	private int getPotionShift(Minecraft mc) {
		if (mc.currentScreen instanceof GuiContainer) {
			GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
			return this.guiLeft - guiContainer.guiLeft;
		}
		return 0;
	}*/
}
