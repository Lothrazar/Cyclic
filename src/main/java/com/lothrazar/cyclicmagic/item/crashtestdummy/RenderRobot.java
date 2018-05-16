package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.RenderUtil;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderRobot extends RenderBiped<EntityRobot> {

  private static final ResourceLocation texture = new ResourceLocation(Const.MODID, "textures/entity/robot.png");
  public RenderRobot(RenderManager renderManagerIn) {
    super(renderManagerIn, new ModelZombie(), 0.5F);
    LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
      @Override
      protected void initArmor() {
        this.modelLeggings = new ModelZombie(0.5F, true);
        this.modelArmor = new ModelZombie(1.0F, true);
      }
    };
    this.addLayer(layerbipedarmor);
  }
  @Override
  public void doRender(EntityRobot entity, double x, double y, double z, float entityYaw, float partialTicks) {
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
    //toggle on right click?
    //    if (EntityRobot.renderDebugHitboxes)
    RenderUtil.renderEntityText(entity, x, y, z, "123");
  }
  @Override
  protected ResourceLocation getEntityTexture(EntityRobot entity) {
    return texture;
  }

  public static class Factory implements IRenderFactory<EntityRobot> {
    @Override
    public Render<? super EntityRobot> createRenderFor(RenderManager manager) {
      return new RenderRobot(manager);
    }
  }
}
