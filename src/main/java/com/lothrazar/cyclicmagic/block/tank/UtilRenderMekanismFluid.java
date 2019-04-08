package com.lothrazar.cyclicmagic.block.tank;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.OpenGlHelper;

/**
 * I learned some of these tips and tricks from Mekanism fluid rendering
 * https://github.com/aidancbrady/Mekanism/blob/f9ec882bcebf685c3b75237bc90d460b217b52a8/src/main/java/mekanism/client/render/MekanismRenderer.java#L429
 *
 */
public class UtilRenderMekanismFluid {

  private static float lightmapLastX;
  private static float lightmapLastY;
  private static boolean optifineBreak = false;

  public static void glowOn(int glow) {
    GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
    try {
      lightmapLastX = OpenGlHelper.lastBrightnessX;
      lightmapLastY = OpenGlHelper.lastBrightnessY;
    }
    catch (NoSuchFieldError e) {
      optifineBreak = true;
    }
    float glowRatioX = Math.min((glow / 15F) * 240F + lightmapLastX, 240);
    float glowRatioY = Math.min((glow / 15F) * 240F + lightmapLastY, 240);
    if (!optifineBreak) {
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, glowRatioX, glowRatioY);
    }
  }

  public static void glowOff() {
    if (!optifineBreak) {
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapLastX, lightmapLastY);
    }
    GL11.glPopAttrib();
  }
}