package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.lothrazar.cyclic.render.RenderResizableCuboid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * legacy ref https://www.minecraftforge.net/forum/topic/79556-1151-rendering-block-manually-clientside/?tab=comments#comment-379808
 */
@SuppressWarnings("deprecation")
public class UtilRender {

  /**
   * used by fluid gui screen rendering Thanks to Mekanism https://github.com/mekanism/Mekanism which uses compatible MIT License
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
  public static void drawTiledSprite(Matrix4f matrix, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite, int textureWidth,
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
    //    RenderSystem.enableAlphaTest();
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
        vertexBuffer.pos(matrix, x, y + textureHeight, zLevel).tex(uMin, vMaxLocal).endVertex();
        vertexBuffer.pos(matrix, shiftedX, y + textureHeight, zLevel).tex(uMaxLocal, vMaxLocal).endVertex();
        vertexBuffer.pos(matrix, shiftedX, y + maskTop, zLevel).tex(uMaxLocal, vMin).endVertex();
        vertexBuffer.pos(matrix, x, y + maskTop, zLevel).tex(uMin, vMin).endVertex();
      }
    }
    vertexBuffer.finishDrawing();
    WorldVertexBufferUploader.draw(vertexBuffer);
    //    RenderSystem.disableAlphaTest();
    RenderSystem.disableBlend();
  }

  private static void renderCube(Matrix4f matrix, IVertexBuilder builder, BlockPos pos, Color color, float alpha) {
    float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
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
  private static void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, IVertexBuilder builder, float red, float green, float blue, float alpha, List<BakedQuad> quads,
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

  /**
   * Used for in-world fluid rendering Source reference from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
   * 
   * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
   * 
   * See MekanismRenderer.
   **/
  public static void renderObject(Model3D object, MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
    if (object != null) {
      RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
    }
  }

  /**
   * used for fluid in-world render lighting
   * 
   * @param light
   * @param fluid
   * @return
   */
  public static int calculateGlowLight(int light, FluidStack fluid) {
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

  public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
    if (fluidStack.isEmpty()) {
      return -1;
    }
    return getColorARGB(fluidStack);
  }

  private static int getColorARGB(FluidStack fluidStack) {
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

  /**
   * Call from TESR perspective
   */
  public static void renderAsBlock(final BlockPos centerPos, final List<BlockPos> shape, MatrixStack matrix, ItemStack stack, float alpha, float scale) {
    BlockState renderBlockState = Block.getBlockFromItem(stack.getItem()).getDefaultState();
    renderAsBlock(centerPos, shape, matrix, renderBlockState, alpha, scale);
  }

  /**
   * Render this BLOCK right here in the world, start with alpha and scale near 1. Call from TESR perspective
   * 
   */
  public static void renderAsBlock(final BlockPos centerPos, final List<BlockPos> shape, MatrixStack matrix, BlockState renderBlockState, float alpha, float scale) {
    World world = Minecraft.getInstance().world;
    //render 
    Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    //
    //    double range = 6F;
    //    ClientPlayerEntity player = Minecraft.getInstance().player;
    //    BlockRayTraceResult lookingAt = (BlockRayTraceResult) player.pick(range, 0F, false);
    //    if (world.isAirBlock(lookingAt.getPos())) {
    //      return;
    //    }
    Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    IVertexBuilder builder = buffer.getBuffer(FakeBlockRenderTypes.FAKE_BLOCK);
    BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
    matrix.push();
    //    BlockPos centerPos = center;//mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.translate(-centerPos.getX(), -centerPos.getY(), -centerPos.getZ());
    for (BlockPos coordinate : shape) {
      //      if (!world.isAirBlock(coordinate)) {
      //        continue;
      //      }
      float x = coordinate.getX();
      float y = coordinate.getY();
      float z = coordinate.getZ();
      matrix.push();
      matrix.translate(x, y, z);
      //
      //shrink it up
      matrix.translate(-0.0005f, -0.0005f, -0.0005f);
      matrix.scale(scale, scale, scale);
      //
      //      UtilWorld.OutlineRenderer.renderHighLightedBlocksOutline(builder, x, y, z, r / 255.0f, g / 255.0f, b / 255.0f, 1.0f); // .02f
      IBakedModel ibakedmodel = dispatcher.getModelForState(renderBlockState);
      BlockColors blockColors = Minecraft.getInstance().getBlockColors();
      int color = blockColors.getColor(renderBlockState, world, coordinate, 0);
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      if (renderBlockState.getRenderType() == BlockRenderType.MODEL) {
        int combinedLights = 15728640;
        int combinedOverlay = 655360;
        for (Direction direction : Direction.values()) {
          UtilRender.renderModelBrightnessColorQuads(matrix.getLast(), builder, red, green, blue, alpha,
              ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(coordinate)), EmptyModelData.INSTANCE),
              combinedLights, combinedOverlay);
        }
        //        UtilRender.renderModelBrightnessColorQuads(matrix.getLast(), builder, red, green, blue, alpha,
        //            ibakedmodel.getQuads(renderBlockState, null, new Random(MathHelper.getPositionRandom(coordinate)), EmptyModelData.INSTANCE),
        //            combinedLights, combinedOverlay);
      }
      matrix.pop();
    }
    ///
    matrix.pop();
  }

  public static void renderOutline(BlockPos view, BlockPos pos, MatrixStack matrix, float scale, Color color) {
    List<BlockPos> coords = new ArrayList<>();
    coords.add(pos);
    renderOutline(view, coords, matrix, scale, color);
  }

  /**
   * Used by TESRs
   * 
   */
  public static void renderOutline(BlockPos view, List<BlockPos> coords, MatrixStack matrix, float scale, Color color) {
    //    IRenderTypeBuffer.getImpl(ibuffer);
    final Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    matrix.push();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    IVertexBuilder builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.SOLID_COLOUR);
    for (BlockPos e : coords) {
      if (e == null) {
        continue;
      }
      //      if (!world.isAirBlock(e)) {
      //        continue;
      //      }
      matrix.push();
      float ctr = (1 - scale) / 2;
      matrix.translate(e.getX() + ctr, e.getY() + ctr, e.getZ() + ctr);
      //      matrix.translate(e.getX() + .5F, e.getY() + .5F, e.getZ() + .5F);
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
      Matrix4f positionMatrix = matrix.getLast().getMatrix();
      UtilRender.renderCube(positionMatrix, builder, e, color, .125F);
      matrix.pop();
    }
    matrix.pop();
    //    RenderSystem.disableDepthTest();
    buffer.finish(FakeBlockRenderTypes.SOLID_COLOUR);
  }

  public static BlockRayTraceResult getLookingAt(PlayerEntity player, int range) {
    return (BlockRayTraceResult) player.pick(range, 0F, false);
  }

  /**
   * for ITEMS held by the PLAYER rendering cubes in world
   * 
   * @param evt
   * @param coords
   * @param alpha
   */
  public static void renderColourCubes(RenderWorldLastEvent evt, Map<BlockPos, Color> coords, float alpha) {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    float scale = 1.01F;
    final Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    MatrixStack matrix = evt.getMatrixStack();
    matrix.push();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    IVertexBuilder builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
    for (BlockPos posCurr : coords.keySet()) {
      matrix.push();
      matrix.translate(posCurr.getX(), posCurr.getY(), posCurr.getZ());
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
      UtilRender.renderCube(matrix.getLast().getMatrix(), builder, posCurr, coords.get(posCurr), alpha);
      matrix.pop();
    }
    matrix.pop();
    RenderSystem.disableDepthTest();
    buffer.finish(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
  }

  /**
   * Box OUTLINE that you can see thru blocks.
   * 
   * From https://github.com/Lothrazar/SimpleTomb/blob/trunk/1.16/src/main/java/com/lothrazar/simpletomb/event/ClientEvents.java
   * 
   */
  public static void createBox(MatrixStack matrixStack, BlockPos pos) {
    final double offset = 1;
    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    Minecraft mc = Minecraft.getInstance();
    RenderSystem.disableTexture();
    RenderSystem.disableBlend();
    RenderSystem.disableDepthTest();
    RenderSystem.pushMatrix();
    Vector3d viewPosition = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    long c = (System.currentTimeMillis() / 15L) % 360L;
    float[] color = getHSBtoRGBF(c / 360f, 1f, 1f);
    matrixStack.push();
    // get a closer pos if too far
    Vector3d vec = new Vector3d(x, y, z).subtract(viewPosition);
    if (vec.distanceTo(Vector3d.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    x -= viewPosition.getX();
    y -= viewPosition.getY();
    z -= viewPosition.getZ();
    RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder renderer = tessellator.getBuffer();
    renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
    RenderSystem.color4f(color[0], color[1], color[2], 1f);
    RenderSystem.lineWidth(2.5f);
    renderer.pos(x, y, z).endVertex();
    renderer.pos(x + offset, y, z).endVertex();
    renderer.pos(x, y, z).endVertex();
    renderer.pos(x, y + offset, z).endVertex();
    renderer.pos(x, y, z).endVertex();
    renderer.pos(x, y, z + offset).endVertex();
    renderer.pos(x + offset, y + offset, z + offset).endVertex();
    renderer.pos(x, y + offset, z + offset).endVertex();
    renderer.pos(x + offset, y + offset, z + offset).endVertex();
    renderer.pos(x + offset, y, z + offset).endVertex();
    renderer.pos(x + offset, y + offset, z + offset).endVertex();
    renderer.pos(x + offset, y + offset, z).endVertex();
    renderer.pos(x, y + offset, z).endVertex();
    renderer.pos(x, y + offset, z + offset).endVertex();
    renderer.pos(x, y + offset, z).endVertex();
    renderer.pos(x + offset, y + offset, z).endVertex();
    renderer.pos(x + offset, y, z).endVertex();
    renderer.pos(x + offset, y, z + offset).endVertex();
    renderer.pos(x + offset, y, z).endVertex();
    renderer.pos(x + offset, y + offset, z).endVertex();
    renderer.pos(x, y, z + offset).endVertex();
    renderer.pos(x + offset, y, z + offset).endVertex();
    renderer.pos(x, y, z + offset).endVertex();
    renderer.pos(x, y + offset, z + offset).endVertex();
    tessellator.draw();
    matrixStack.pop();
    RenderSystem.popMatrix();
    RenderSystem.lineWidth(1f);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.enableTexture();
    //    RenderSystem.color4f(1f, 1f, 1f, 1f);
  }

  /**
   * From https://github.com/Lothrazar/SimpleTomb/blob/704bad5a33731125285d700c489bfe2c3a9e387d/src/main/java/com/lothrazar/simpletomb/helper/WorldHelper.java#L163
   * 
   * @param hue
   * @param saturation
   * @param brightness
   * @return
   */
  public static float[] getHSBtoRGBF(float hue, float saturation, float brightness) {
    int r = 0;
    int g = 0;
    int b = 0;
    if (saturation == 0.0F) {
      r = g = b = (int) (brightness * 255.0F + 0.5F);
    }
    else {
      float h = (hue - (float) Math.floor(hue)) * 6.0F;
      float f = h - (float) Math.floor(h);
      float p = brightness * (1.0F - saturation);
      float q = brightness * (1.0F - saturation * f);
      float t = brightness * (1.0F - saturation * (1.0F - f));
      switch ((int) h) {
        case 0:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (t * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
        break;
        case 1:
          r = (int) (q * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
        break;
        case 2:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (t * 255.0F + 0.5F);
        break;
        case 3:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (q * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
        break;
        case 4:
          r = (int) (t * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
        break;
        case 5:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (q * 255.0F + 0.5F);
      }
    }
    return new float[] {
        r / 255.0F,
        g / 255.0F,
        b / 255.0F,
    };
  }
}
