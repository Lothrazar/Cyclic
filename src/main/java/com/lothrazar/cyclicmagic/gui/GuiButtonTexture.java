package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTexture extends GuiButton {
  public static ResourceLocation icon;
  public GuiButtonTexture(int buttonId, int x, int y, String texture) {
    super(buttonId, x, y, 16, 20, "");
    icon = new ResourceLocation(Const.MODID, texture);
  }
  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (this.visible) {
      super.drawButton(mc, mouseX, mouseY);
      mc.getTextureManager().bindTexture(icon);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      this.drawTexturedModalRect(this.xPosition, // + this.width / 2, 
          this.yPosition,
          0, 0,
          16, 16);
      this.mouseDragged(mc, mouseX, mouseY);
    }
  }
}
