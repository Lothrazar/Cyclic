package com.lothrazar.cyclicmagic.gui.base;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTexture extends GuiButtonTooltip {
  private ResourceLocation icon;
  private int textureIndex = 0;
  private int textureSize = 16;
  public GuiButtonTexture(int buttonId, int x, int y, String texture) {
    super(buttonId, x, y, 18, 20, "");
    icon = new ResourceLocation(Const.MODID, texture);
  }
  protected void setTextureIndex(int i) {
    textureIndex = i;
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
      this.drawTexturedModalRect(this.xPosition + 1, // +1 since button is 18 wide and texture is 16
          this.yPosition,
          textureIndex * textureSize, 0,
          textureSize, textureSize);
      this.mouseDragged(mc, mouseX, mouseY);
    }
  }
}
