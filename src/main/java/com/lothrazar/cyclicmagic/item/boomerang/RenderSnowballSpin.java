package com.lothrazar.cyclicmagic.item.boomerang;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSnowballSpin<T extends Entity> extends RenderSnowball<T> {

  private RenderItem itemRenderer;
  float angle = 0;

  public RenderSnowballSpin(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
    super(renderManagerIn, itemIn, itemRendererIn);
    this.itemRenderer = itemRendererIn;
  }

  @Override
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    //    super.doRender(entity, x, y, z, entityYaw, partialTicks);
    GlStateManager.pushMatrix();
    GlStateManager.translate((float) x, (float) y, (float) z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    angle += 15.0;
    if (angle > 180) {
      angle = -180;
    }
    GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);

    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }
    this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
  }
}
