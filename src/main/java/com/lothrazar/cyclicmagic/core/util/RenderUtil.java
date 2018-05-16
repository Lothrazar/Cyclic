package com.lothrazar.cyclicmagic.core.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderUtil {

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

      float s = 0.1F;
      GlStateManager.rotate(180, 1, 0, 0);
      GlStateManager.translate(-.5F, -3.5F + time / 100, -.5F);
      GlStateManager.scale(s, s, s);
      Minecraft.getMinecraft().fontRenderer.drawString(custom, 0, 0, 0xFFFFFF);
      GlStateManager.popMatrix();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_LIGHTING);

    }
  }
}
