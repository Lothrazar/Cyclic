package com.lothrazar.cyclic.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclic.data.Model3D;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.fluids.FluidStack;

/**
 * legacy ref https://www.minecraftforge.net/forum/topic/79556-1151-rendering-block-manually-clientside/?tab=comments#comment-379808
 */
public class RenderUtils {

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
    //    Minecraft.getInstance().textureManager.bind(InventoryMenu.BLOCK_ATLAS);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    int xTileCount = desiredWidth / textureWidth;
    int xRemainder = desiredWidth - (xTileCount * textureWidth);
    int yTileCount = desiredHeight / textureHeight;
    int yRemainder = desiredHeight - (yTileCount * textureHeight);
    int yStart = yPosition + yOffset;
    float uMin = sprite.getU0();
    float uMax = sprite.getU1();
    float vMin = sprite.getV0();
    float vMax = sprite.getV1();
    float uDif = uMax - uMin;
    float vDif = vMax - vMin;
    RenderSystem.enableBlend();
    //    RenderSystem.enableAlphaTest();
    BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
    vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    //    vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
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
        vertexBuffer.vertex(matrix, x, y + textureHeight, zLevel).uv(uMin, vMaxLocal).endVertex();
        vertexBuffer.vertex(matrix, shiftedX, y + textureHeight, zLevel).uv(uMaxLocal, vMaxLocal).endVertex();
        vertexBuffer.vertex(matrix, shiftedX, y + maskTop, zLevel).uv(uMaxLocal, vMin).endVertex();
        vertexBuffer.vertex(matrix, x, y + maskTop, zLevel).uv(uMin, vMin).endVertex();
      }
    }
    vertexBuffer.end();
    BufferUploader.end(vertexBuffer);
    //    RenderSystem.disableAlphaTest();
    RenderSystem.disableBlend();
  }

  private static void renderCube(Matrix4f matrix, VertexConsumer builder, BlockPos pos, Color color, float alpha) {
    float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
    float startX = 0, startY = 0, startZ = -1, endX = 1, endY = 1, endZ = 0;
    //down
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //up
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    //east
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    //west
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    //south
    builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
    //north
    builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
    builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
  }

  /**
   * This block-rendering function from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
   */
  private static void renderModelBrightnessColorQuads(PoseStack.Pose matrixEntry, VertexConsumer builder, float red, float green, float blue, float alpha, List<BakedQuad> quads,
      int combinedLights, int combinedOverlay) {
    for (BakedQuad bakedquad : quads) {
      float r;
      float g;
      float b;
      if (bakedquad.isTinted()) {
        r = red * 1f;
        g = green * 1f;
        b = blue * 1f;
      }
      else {
        r = 1f;
        g = 1f;
        b = 1f;
      }
      //      addVertexData
      builder.putBulkData(matrixEntry, bakedquad, r, g, b, alpha, combinedLights, combinedOverlay);
    }
  }

  /**
   * Used for in-world fluid rendering Source reference from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
   * <p>
   * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
   * <p>
   * See MekanismRenderer.
   **/
  public static void renderObject(Model3D object, PoseStack matrix, VertexConsumer buffer, int argb, int light) {
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
    int blockLight = LightTexture.block(light);
    int skyLight = LightTexture.sky(light);
    return LightTexture.pack(Math.max(blockLight, glow), Math.max(skyLight, glow));
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
  public static void renderAsBlock(final BlockPos centerPos, final List<BlockPos> shape, PoseStack matrix, ItemStack stack, float alpha, float scale) {
    BlockState renderBlockState = Block.byItem(stack.getItem()).defaultBlockState();
    renderAsBlock(centerPos, shape, matrix, renderBlockState, alpha, scale);
  }

  /**
   * Render this BLOCK right here in the world, start with alpha and scale near 1. Call from TESR perspective
   */
  public static void renderAsBlock(final BlockPos centerPos, final List<BlockPos> shape, PoseStack matrix, BlockState renderBlockState, float alpha, float scale) {
    Level world = Minecraft.getInstance().level;
    //render 
    //    Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    //
    //    double range = 6F;
    //    ClientPlayerEntity player = Minecraft.getInstance().player;
    //    BlockRayTraceResult lookingAt = (BlockRayTraceResult) player.pick(range, 0F, false);
    //    if (world.isAirBlock(lookingAt.getPos())) {
    //      return;
    //    }
    Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    VertexConsumer builder = buffer.getBuffer(FakeBlockRenderTypes.FAKE_BLOCK);
    BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
    matrix.pushPose();
    //    BlockPos centerPos = center;//mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.translate(-centerPos.getX(), -centerPos.getY(), -centerPos.getZ());
    for (BlockPos coordinate : shape) {
      //      if (!world.isAirBlock(coordinate)) {
      //        continue;
      //      }
      float x = coordinate.getX();
      float y = coordinate.getY();
      float z = coordinate.getZ();
      matrix.pushPose();
      matrix.translate(x, y, z);
      //
      //shrink it up
      matrix.translate(-0.0005f, -0.0005f, -0.0005f);
      matrix.scale(scale, scale, scale);
      //
      //      UtilWorld.OutlineRenderer.renderHighLightedBlocksOutline(builder, x, y, z, r / 255.0f, g / 255.0f, b / 255.0f, 1.0f); // .02f
      BakedModel ibakedmodel = dispatcher.getBlockModel(renderBlockState);
      BlockColors blockColors = Minecraft.getInstance().getBlockColors();
      int color = blockColors.getColor(renderBlockState, world, coordinate, 0);
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      if (renderBlockState.getRenderShape() == RenderShape.MODEL) {
        int combinedLights = 15728640;
        int combinedOverlay = 655360;
        for (Direction direction : Direction.values()) {
          RenderUtils.renderModelBrightnessColorQuads(matrix.last(), builder, red, green, blue, alpha,
              ibakedmodel.getQuads(renderBlockState, direction, RandomSource.create(Mth.getSeed(coordinate))), // EmptyModelData.INSTANCE 
              combinedLights, combinedOverlay);
        }
        //        UtilRender.renderModelBrightnessColorQuads(matrix.getLast(), builder, red, green, blue, alpha,
        //            ibakedmodel.getQuads(renderBlockState, null, new Random(MathHelper.getPositionRandom(coordinate)), EmptyModelData.INSTANCE),
        //            combinedLights, combinedOverlay);
      }
      matrix.popPose();
    }
    ///
    matrix.popPose();
  }

  public static void renderOutline(BlockPos view, BlockPos pos, PoseStack matrix, float scale, Color color) {
    List<BlockPos> coords = new ArrayList<>();
    coords.add(pos);
    renderOutline(view, coords, matrix, scale, color);
  }

  /**
   * Used by TESRs
   */
  public static void renderOutline(BlockPos view, List<BlockPos> coords, PoseStack matrix, float scale, Color color) {
    //    IRenderTypeBuffer.getImpl(ibuffer);
    final Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    matrix.pushPose();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    VertexConsumer builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.SOLID_COLOUR);
    for (BlockPos e : coords) {
      if (e == null) {
        continue;
      }
      //      if (!world.isAirBlock(e)) {
      //        continue;
      //      }
      matrix.pushPose();
      float ctr = (1 - scale) / 2;
      matrix.translate(e.getX() + ctr, e.getY() + ctr, e.getZ() + ctr);
      //      matrix.translate(e.getX() + .5F, e.getY() + .5F, e.getZ() + .5F);
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
      Matrix4f positionMatrix = matrix.last().pose();
      RenderUtils.renderCube(positionMatrix, builder, e, color, .125F);
      matrix.popPose();
    }
    matrix.popPose();
    //    RenderSystem.disableDepthTest();
    buffer.endBatch(FakeBlockRenderTypes.SOLID_COLOUR);
  }

  public static BlockHitResult getLookingAt(Player player, int range) {
    return (BlockHitResult) player.pick(range, 0F, false);
  }

  /**
   * for ITEMS held by the PLAYER rendering cubes in world
   *
   * @param evt
   * @param coords
   * @param alpha
   */
  public static void renderColourCubes(RenderLevelLastEvent evt, Map<BlockPos, Color> coords, float alpha) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    float scale = 1.01F;
    final Minecraft mc = Minecraft.getInstance();
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    Vec3 view = mc.gameRenderer.getMainCamera().getPosition();
    PoseStack matrix = evt.getPoseStack();
    matrix.pushPose();
    matrix.translate(-view.x(), -view.y(), -view.z());
    VertexConsumer builder = buffer.getBuffer(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
    for (BlockPos posCurr : coords.keySet()) {
      matrix.pushPose();
      matrix.translate(posCurr.getX(), posCurr.getY(), posCurr.getZ());
      matrix.translate(-0.005f, -0.005f, -0.005f);
      matrix.scale(scale, scale, scale);
      matrix.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
      RenderUtils.renderCube(matrix.last().pose(), builder, posCurr, coords.get(posCurr), alpha);
      matrix.popPose();
    }
    matrix.popPose();
    RenderSystem.disableDepthTest();
    buffer.endBatch(FakeBlockRenderTypes.TRANSPARENT_COLOUR);
  }

  public static void createBox(PoseStack poseStack, BlockPos pos) {
    poseStack.pushPose();
    createBox(Minecraft.getInstance().renderBuffers().bufferSource(), poseStack, pos.getX(), pos.getY(), pos.getZ(), 1.0F);
    poseStack.popPose();
  }

  private static void createBox(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, float x, float y, float z, float offset) {
    long c = (System.currentTimeMillis() / 15L) % 360L;
    float[] color = getHSBtoRGBF(c / 360f, 1f, 1f);
    Minecraft mc = Minecraft.getInstance();
    Vec3 cameraPosition = mc.gameRenderer.getMainCamera().getPosition();
    // get a closer pos if too far
    Vec3 vec = new Vec3(x, y, z).subtract(cameraPosition);
    if (vec.distanceTo(Vec3.ZERO) > 200d) { // could be 300
      vec = vec.normalize().scale(200d);
      x += vec.x;
      y += vec.y;
      z += vec.z;
    }
    RenderSystem.disableDepthTest();
    VertexConsumer vertexConsumer = bufferSource.getBuffer(FakeBlockRenderTypes.TOMB_LINES);
    poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
    Matrix4f pose = poseStack.last().pose();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y + offset, z).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x + offset, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    vertexConsumer.vertex(pose, x, y + offset, z + offset).color(color[0], color[1], color[2], 1.0F).endVertex();
    bufferSource.endBatch(FakeBlockRenderTypes.TOMB_LINES);
    RenderSystem.enableDepthTest();
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
