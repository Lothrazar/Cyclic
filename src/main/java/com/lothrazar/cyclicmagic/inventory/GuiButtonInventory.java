package com.lothrazar.cyclicmagic.inventory;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonInventory extends GuiButton{

  private String text;
	public GuiButtonInventory(int buttonId, int guiLeft, int guiTop, int x, int y, int width, int height, String buttonText) {
		super(buttonId, guiLeft + x, guiTop + y, width, height, "I");
		//this.guiLeft = guiLeft;
		text = buttonText;

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
			int potionShift = 0;//getPotionShift(mc);

			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(GuiPlayerExtended.background);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = xx >= this.xPosition + potionShift && yy >= this.yPosition && xx < this.xPosition + this.width + potionShift && yy < this.yPosition + this.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if(this.getHoverState(this.hovered)==1){
				this.drawCenteredString(fontrenderer, text, this.xPosition + 5 + potionShift, this.yPosition + this.height, 0xffffff);
			}

			this.mouseDragged(mc, xx, yy);
			
			
		}
	}
	*/
	
/*
	private int getPotionShift(Minecraft mc) {
		if (mc.currentScreen instanceof GuiContainer) {
			GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
			return this.guiLeft - guiContainer.guiLeft;
		}
		return 0;
	}*/
}
