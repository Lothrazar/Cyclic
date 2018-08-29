package com.lothrazar.cyclicmagic.core.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderUtil {

  public static final int MAX_LIGHT_X = 0xF000F0;
  public static final int MAX_LIGHT_Y = 0xF000F0;

  @SideOnly(Side.CLIENT)
  public static void renderLaser(BlockPos first, BlockPos second,
      double rotationTime, float alpha, double beamWidth, float[] color) {
    RenderUtil.renderLaser(
        first.getX() + 0.5, first.getY() + 0.5, first.getZ() + 0.5,
        second.getX() + 0.5, second.getY() + 0.5, second.getZ() + 0.5,
        rotationTime, alpha, beamWidth, color);
  }

  //I got this from ActuallyAdditions by Ellpeck 
  // source https://github.com/Ellpeck/ActuallyAdditions/blob/08d0e8b7fb463054e3f392ddbb2a2ca2e2877000/src/main/java/de/ellpeck/actuallyadditions/mod/util/AssetUtil.java#L257
  // who in turn left their source where they got it, copied verabitm: 
  //Thanks to feldim2425 for this.
  //I can't do rendering code. Ever.
  @SideOnly(Side.CLIENT)
  public static void renderLaser(double firstX, double firstY, double firstZ,
      double secondX, double secondY, double secondZ,
      double rotationTime, float alpha, double beamWidth, float[] color) {
    Tessellator tessy = Tessellator.getInstance();
    BufferBuilder render = tessy.getBuffer();
    World world = Minecraft.getMinecraft().world;
    float r = color[0];
    float g = color[1];
    float b = color[2];
    Vec3d vec1 = new Vec3d(firstX, firstY, firstZ);
    Vec3d vec2 = new Vec3d(secondX, secondY, secondZ);
    Vec3d combinedVec = vec2.subtract(vec1);
    double rot = rotationTime > 0 ? (360D * ((world.getTotalWorldTime() % rotationTime) / rotationTime)) : 0;
    double pitch = Math.atan2(combinedVec.y, Math.sqrt(combinedVec.x * combinedVec.x + combinedVec.z * combinedVec.z));
    double yaw = Math.atan2(-combinedVec.z, combinedVec.x);
    double length = combinedVec.lengthVector();
    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
    int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
    float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
    GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
    GlStateManager.translate(firstX - TileEntityRendererDispatcher.staticPlayerX, firstY - TileEntityRendererDispatcher.staticPlayerY, firstZ - TileEntityRendererDispatcher.staticPlayerZ);
    GlStateManager.rotate((float) (180 * yaw / Math.PI), 0, 1, 0);
    GlStateManager.rotate((float) (180 * pitch / Math.PI), 0, 0, 1);
    GlStateManager.rotate((float) rot, 1, 0, 0);
    GlStateManager.disableTexture2D();
    render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
    for (double i = 0; i < 4; i++) {
      double width = beamWidth * (i / 4.0);
      render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, -width, width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(0, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
      render.pos(length, -width, -width).tex(0, 0).lightmap(MAX_LIGHT_X, MAX_LIGHT_Y).color(r, g, b, alpha).endVertex();
    }
    tessy.draw();
    GlStateManager.enableTexture2D();

    GlStateManager.alphaFunc(func, ref);
    GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.disableBlend();
    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
  }
}
