package com.lothrazar.cyclicmagic.component.cablefluid;
import com.lothrazar.cyclicmagic.component.cable.ModelCable;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class CableFluidRenderer extends TileEntitySpecialRenderer<TileEntityFluidCable> {
  ModelCable model;
  private final ResourceLocation link = new ResourceLocation(Const.MODID, "textures/tile/fluid.png");
  //  private final ResourceLocation storage = new ResourceLocation(StorageNetwork.MODID + ":textures/tile/storage.png");
  public CableFluidRenderer() {
    model = new ModelCable();
  }
  @Override
  public void render(TileEntityFluidCable te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
    if (te == null) {//|| te.getKind() == null || !(te.getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockCable)) {
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
