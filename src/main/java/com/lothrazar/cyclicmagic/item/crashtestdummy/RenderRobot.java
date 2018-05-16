package com.lothrazar.cyclicmagic.item.crashtestdummy;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.item.crashtestdummy.EntityRobot.DmgTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    for (DmgTracker tr : entity.trackers) {
      if (tr.timer > 0 && tr.message != "")
        renderEntityText(entity, x, y, z, tr.message, tr.timer);
  }
  }

  @SideOnly(Side.CLIENT)
  public static void renderEntityText(EntityLiving entity, double x, double y, double z, String custom, float time) {
    AxisAlignedBB bb = entity.getEntityBoundingBox();
    if (bb != null) {
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_CULL_FACE);
      GL11.glPushMatrix();
      GL11.glTranslatef((float) x, (float) y, (float) z);
      GL11.glPushMatrix();
      GL11.glRotatef(-entity.renderYawOffset, 0, 1, 0);
      GlStateManager.pushMatrix();
      float s = 0.05F; //scale size
      float step = 1.105F;//movement speed

      float pct = (EntityRobot.MAX_TIMER - time) / EntityRobot.MAX_TIMER;

      float ystart = -2.2F;
      y = ystart - pct * step;
      GlStateManager.rotate(180, 1, 0, 0);
      GlStateManager.translate(-.5F, y, -.5F);
      GlStateManager.scale(s, s, s);
      Minecraft.getMinecraft().fontRenderer.drawString(custom, 0, 0, 0xFFFFFF);
      GlStateManager.popMatrix();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_LIGHTING);
    }
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
