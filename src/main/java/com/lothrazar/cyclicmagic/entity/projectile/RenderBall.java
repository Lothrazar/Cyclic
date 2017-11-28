package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Similar to RenderSnowball, except it uses any texture resource location, does not have to be an item. not depending on the item baked model
 * 
 * @author Sam
 *
 * @param <T>
 */
public class RenderBall<T extends Entity> extends Render<T> {
  private ResourceLocation resource;
  public RenderBall(RenderManager renderManager, String texture) {
    super(renderManager);
    resource = new ResourceLocation(Const.MODID, "textures/entity/projectile/" + texture + ".png");
  }
  @Override
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    this.bindEntityTexture(entity);
    GlStateManager.translate((float) x, (float) y, (float) z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
    bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
    tessellator.draw();
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  @Override
  protected ResourceLocation getEntityTexture(T entity) {
    return resource;
  }
}
