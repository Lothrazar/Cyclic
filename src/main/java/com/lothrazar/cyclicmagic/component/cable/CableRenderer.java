package com.lothrazar.cyclicmagic.component.cable;
import com.lothrazar.cyclicmagic.component.cable.bundle.TileEntityCableBundle;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * TODO: shared base class with fluid
 *
 */
public class CableRenderer extends TileEntitySpecialRenderer<TileEntityBaseCable> {
  ModelCable model;
  private  ResourceLocation link ;
  public CableRenderer(ResourceLocation texture) {
    model = new ModelCable();
    link = texture;
  }
  @Override
  public void render(TileEntityBaseCable te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
    if (te == null) {
      return;
    }
    GlStateManager.pushMatrix();
    GlStateManager.enableRescaleNormal();
    GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
    Minecraft.getMinecraft().renderEngine.bindTexture(link);
    GlStateManager.pushMatrix();
    GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.pushAttrib();
    RenderHelper.disableStandardItemLighting();
    model.render(te);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popAttrib();
    GlStateManager.popMatrix();
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
