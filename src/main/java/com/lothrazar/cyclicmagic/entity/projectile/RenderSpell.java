package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.model.ModelLlamaSpit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSpell<T extends Entity> extends Render<T> {
 
  private final ModelLlamaSpit model = new ModelLlamaSpit();
  protected RenderSpell(RenderManager renderManager) {
    super(renderManager);
  }
  @Override
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float) x, (float) y + 0.15F, (float) z);
    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
    this.bindEntityTexture(entity);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }
    this.model.render(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  @Override
  protected ResourceLocation getEntityTexture(T entity) {
    return new ResourceLocation(Const.MODID, "textures/entity/spit.png");
  }
}
