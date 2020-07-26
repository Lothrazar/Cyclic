package com.lothrazar.cyclic.util;

import java.awt.Color;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.data.OffsetEnum;
import com.lothrazar.cyclic.render.RenderResizableCuboid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

public class UtilRender {

  /**
   * Thanks to Mekanism https://github.com/mekanism/Mekanism which uses compatible MIT License
   * 
   * @param xPosition
   * @param yPosition
   * @param yOffset
   * @param desiredWidth
   * @param desiredHeight
   * @param sprite
   * @param textureWidth
   * @param textureHeight
   * @param zLevel
   */
  public static void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth,
      int textureHeight, int zLevel) {
    if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
      return;
    }
    Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    int xTileCount = desiredWidth / textureWidth;
    int xRemainder = desiredWidth - (xTileCount * textureWidth);
    int yTileCount = desiredHeight / textureHeight;
    int yRemainder = desiredHeight - (yTileCount * textureHeight);
    int yStart = yPosition + yOffset;
    float uMin = sprite.getMinU();
    float uMax = sprite.getMaxU();
    float vMin = sprite.getMinV();
    float vMax = sprite.getMaxV();
    float uDif = uMax - uMin;
    float vDif = vMax - vMin;
    RenderSystem.enableBlend();
    RenderSystem.enableAlphaTest();
    BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
    vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    for (int xTile = 0; xTile <= xTileCount; xTile++) {
      int width = (xTile == xTileCount) ? xRemainder : textureWidth;
      if (width == 0) {
        break;
      }
      int x = xPosition + (xTile * textureWidth);
      int maskRight = textureWidth - width;
      int shiftedX = x + textureWidth - maskRight;
      float uMaxLocal = uMax - (uDif * maskRight / textureWidth);
      for (int yTile = 0; yTile <= yTileCount; yTile++) {
        int height = (yTile == yTileCount) ? yRemainder : textureHeight;
        if (height == 0) {
          //Note: We don't want to fully break out because our height will be zero if we are looking to
          // draw the remainder, but there is no remainder as it divided evenly
          break;
        }
        int y = yStart - ((yTile + 1) * textureHeight);
        int maskTop = textureHeight - height;
        float vMaxLocal = vMax - (vDif * maskTop / textureHeight);
        vertexBuffer.pos(x, y + textureHeight, zLevel).tex(uMin, vMaxLocal).endVertex();
        vertexBuffer.pos(shiftedX, y + textureHeight, zLevel).tex(uMaxLocal, vMaxLocal).endVertex();
        vertexBuffer.pos(shiftedX, y + maskTop, zLevel).tex(uMaxLocal, vMin).endVertex();
        vertexBuffer.pos(x, y + maskTop, zLevel).tex(uMin, vMin).endVertex();
      }
    }
    vertexBuffer.finishDrawing();
    WorldVertexBufferUploader.draw(vertexBuffer);
    RenderSystem.disableAlphaTest();
    RenderSystem.disableBlend();
  }

  public static void renderCube(Matrix4f matrix, IVertexBuilder builder, BlockPos pos, Color color) {
    float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f, alpha = .125f;
    float startX = 0, startY = 0, startZ = -1, endX = 1, endY = 1, endZ = 0;
    //down
    builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //up
    builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    //east
    builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    //west
    builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    //south
    builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //north
    builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
  }

  /**
   * This block-rendering function from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
   *
   */
  public static void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, IVertexBuilder builder, float red, float green, float blue, float alpha, List<BakedQuad> quads,
      int combinedLights, int combinedOverlay) {
    for (BakedQuad bakedquad : quads) {
      float r;
      float g;
      float b;
      if (bakedquad.hasTintIndex()) {
        r = red * 1f;
        g = green * 1f;
        b = blue * 1f;
      }
      else {
        r = 1f;
        g = 1f;
        b = 1f;
      }
      builder.addVertexData(matrixEntry, bakedquad, r, g, b, alpha, combinedLights, combinedOverlay);
    }
  }

  public static class LaserConfig {

    public static final int MAX_TIMER = 100;

    public LaserConfig(BlockPos first, BlockPos second,
        double rotationTime, float alpha, double beamWidth, float[] color) {
      this.first = first;
      this.second = second;
      this.rotationTime = rotationTime;
      this.alpha = alpha;
      this.beamWidth = beamWidth;
      this.color = color;
    }

    BlockPos first;
    BlockPos second;
    double rotationTime;
    float alpha;
    double beamWidth;
    float[] color;
    public int timer = LaserConfig.MAX_TIMER;
    public OffsetEnum xOffset = OffsetEnum.CENTER;
    public OffsetEnum yOffset = OffsetEnum.CENTER;
    public OffsetEnum zOffset = OffsetEnum.CENTER;

    @Override
    public String toString() {
      return second + " : " + first;
    }
  }

  public static final int MAX_LIGHT_X = 0xF000F0;
  public static final int MAX_LIGHT_Y = MAX_LIGHT_X;

  @OnlyIn(Dist.CLIENT)
  public static void renderLaser(LaserConfig conf, MatrixStack matrixStack) {
    if (conf.first == null || conf.second == null) {
      return;
    }
    double offsetX = conf.xOffset.getOffset();
    double offsetY = conf.yOffset.getOffset();
    double offsetZ = conf.zOffset.getOffset();
    UtilRender.renderLaser(
        conf.first.getX() + offsetX, conf.first.getY() + offsetY, conf.first.getZ() + offsetZ,
        conf.second.getX() + offsetX, conf.second.getY() + offsetY, conf.second.getZ() + offsetZ,
        conf.rotationTime, conf.alpha, conf.beamWidth, conf.color, conf.timer, matrixStack);
  }

  //I got this function from ActuallyAdditions by Ellpeck 
  // source https://github.com/Ellpeck/ActuallyAdditions/blob/08d0e8b7fb463054e3f392ddbb2a2ca2e2877000/src/main/java/de/ellpeck/actuallyadditions/mod/util/AssetUtil.java#L257
  // who in turn left their source where they got it, copied verabitm: 
  //Thanks to feldim2425 for this.
  //I can't do rendering code. Ever.
  @OnlyIn(Dist.CLIENT)
  public static void renderLaser(double firstX, double firstY, double firstZ,
      double secondX, double secondY, double secondZ,
      double rotationTime, float alpha, double beamWidth, float[] color, double timer, MatrixStack matrixStack) {
    Tessellator tessy = Tessellator.getInstance();
    BufferBuilder buffer = tessy.getBuffer();
    World world = Minecraft.getInstance().world;
    float r = color[0];
    float g = color[1];
    float b = color[2];
    Vector3d vecFirst = new Vector3d(firstX, firstY, firstZ);
    Vector3d vecSecond = new Vector3d(secondX, secondY, secondZ);
    Vector3d combinedVec = vecSecond.subtract(vecFirst);
    //    world.getGameTime()getTotalWorldTime
    double rot = rotationTime > 0 ? (360D * ((world.getGameTime() % rotationTime) / rotationTime)) : 0;
    double pitch = Math.atan2(combinedVec.y, Math.sqrt(combinedVec.x * combinedVec.x + combinedVec.z * combinedVec.z));
    double yaw = Math.atan2(-combinedVec.z, combinedVec.x);
    float length = (float) combinedVec.length();
    length = (float) (length * (timer / (LaserConfig.MAX_TIMER * 1.0F)));
    RenderSystem.pushMatrix();
    RenderSystem.rotatef((float) (180 * yaw / Math.PI), 0, 1, 0);
    RenderSystem.rotatef((float) (180 * pitch / Math.PI), 0, 0, 1);
    RenderSystem.rotatef((float) rot, 1, 0, 0);
    PlayerEntity player = ModCyclic.proxy.getClientPlayer();
    ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
    double staticPlayerX = player.lastTickPosX;
    double staticPlayerY = player.lastTickPosY;
    double staticPlayerZ = player.lastTickPosZ;
    staticPlayerX = renderInfo.getProjectedView().getX();
    staticPlayerY = renderInfo.getProjectedView().getY();
    staticPlayerZ = renderInfo.getProjectedView().getZ();
    //    RenderSystem.translated(firstX - staticPlayerX, firstY - staticPlayerY, firstZ - staticPlayerZ);
    //    
    //    
    //    
    matrixStack.push(); // push
    matrixStack.translate(secondX - staticPlayerX, secondY - staticPlayerY, secondZ - staticPlayerZ); // translate back to camera
    Matrix4f matrix4f = matrixStack.getLast().getMatrix(); // get final transformation matrix, handy to get yaw+pitch transformation
    RenderSystem.multMatrix(matrix4f);
    //    RenderSystem.translated(secondX - staticPlayerX, secondY - staticPlayerY, secondZ - staticPlayerZ);
    //    GL11.glTranslated(staticPlayerX, staticPlayerY, staticPlayerZ);
    //        RenderSystem.translated(firstX - TileEntityRendererDispatcher.staticPlayerX, firstY - TileEntityRendererDispatcher.staticPlayerY, firstZ - TileEntityRendererDispatcher.staticPlayerZ);
    RenderSystem.disableTexture();
    //    RenderSystem.disableTexture2D();
    RenderSystem.disableLighting();
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    //    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
    for (double i = 0; i < 4; i++) {//four corners of the quad
      float width = (float) (beamWidth * (i / 4.0F));
      // pos == .pos
      //          tex == .tex// for UR
      //func_227885_a_ == color
      // .lightmap(MAX_LIGHT_X, MAX_LIGHT_Y) ==  I DONT KNOW maybe tex(MAX_LIGHT_X, MAX_LIGHT_Y).
      buffer.pos(length, width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, -width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, -width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, -width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, -width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, -width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, -width, width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(0, -width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
      buffer.pos(length, -width, -width).lightmap(0, 0).color(r, g, b, alpha).endVertex();
    }
    tessy.draw();
    matrixStack.pop(); // pop
    RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.disableBlend();
    RenderSystem.enableLighting();
    RenderSystem.disableTexture();
    //    RenderSystem.enableTexture2D();
    RenderSystem.popMatrix();
  }

  /**
   * SHOUTOUT THANK YOU https://www.minecraftforge.net/forum/topic/79556-1151-rendering-block-manually-clientside/?tab=comments#comment-379808
   */
  public static void drawBlock(final BufferBuilder bufferbuilder, final double x, final double y, final double z, final float minU, final float maxU, final float minV, final float maxV,
      final double x_size, final double y_size, final double z_size) {
    // UP
    bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
    bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(maxU, minV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(minU, minV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(minU, maxV).endVertex();
    // DOWN
    bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
    bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
    bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
    bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();
    // LEFT
    bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();
    bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(minU, maxV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(minU, minV).endVertex();
    // RIGHT
    bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
    bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
    bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(maxU, minV).endVertex();
    bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
    // BACK
    bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
    bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(minU, minV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(maxU, minV).endVertex();
    bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
    // FRONT
    bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();
    bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(maxU, maxV).endVertex();
    bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(minU, maxV).endVertex();
    bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
  }

  /**
   * Source furnctions from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
   * 
   * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
   * 
   * See MekanismRenderer.
   **/
  public static void renderObject(@Nullable Model3D object, @Nonnull MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
    if (object != null) {
      RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
    }
  }

  public static int calculateGlowLight(int light, @Nonnull FluidStack fluid) {
    return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
  }

  // Replace various usages of this with the getter for calculating glow light, at least if we end up making it only
  // effect block light for the glow rather than having it actually become full light
  public static final int FULL_LIGHT = 0xF000F0;

  public static int calculateGlowLight(int light, int glow) {
    if (glow >= 15) {
      return FULL_LIGHT;
    }
    int blockLight = LightTexture.getLightBlock(light);
    int skyLight = LightTexture.getLightSky(light);
    return LightTexture.packLight(Math.max(blockLight, glow), Math.max(skyLight, glow));
  }

  public static int getColorARGB(@Nonnull FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    return getColorARGB(fluidStack);
  }

  private static int getColorARGB(@Nonnull FluidStack fluidStack) {
    return fluidStack.getFluid().getAttributes().getColor(fluidStack);
  }

  public static float getRed(int color) {
    return (color >> 16 & 0xFF) / 255.0F;
  }

  public static float getGreen(int color) {
    return (color >> 8 & 0xFF) / 255.0F;
  }

  public static float getBlue(int color) {
    return (color & 0xFF) / 255.0F;
  }

  public static float getAlpha(int color) {
    return (color >> 24 & 0xFF) / 255.0F;
  }
}
