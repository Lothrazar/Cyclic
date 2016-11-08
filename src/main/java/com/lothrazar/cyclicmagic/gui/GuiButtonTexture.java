package com.lothrazar.cyclicmagic.gui;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTexture extends GuiButton implements ITooltipButton{
  private ResourceLocation icon;
  private List<String> tooltip = new ArrayList<String>();
  public GuiButtonTexture(int buttonId, int x, int y, String texture, String ttip) {
    super(buttonId, x, y, 16, 20, "");
    icon = new ResourceLocation(Const.MODID, texture);
    tooltip.add(UtilChat.lang(ttip));
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
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
}
