package com.lothrazar.cyclic.util;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.OffsetEnum;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderUtil {

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
    RenderUtil.renderLaser(
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
    Vec3d vecFirst = new Vec3d(firstX, firstY, firstZ);
    Vec3d vecSecond = new Vec3d(secondX, secondY, secondZ);
    Vec3d combinedVec = vecSecond.subtract(vecFirst);
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
    RenderSystem.translated(firstX - staticPlayerX, firstY - staticPlayerY, firstZ - staticPlayerZ);
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
      // func_225582_a_ == .pos
      //          func_225583_a_ == .tex// for UR
      //func_227885_a_ == color
      // .lightmap(MAX_LIGHT_X, MAX_LIGHT_Y) ==  I DONT KNOW maybe func_225583_a_(MAX_LIGHT_X, MAX_LIGHT_Y).
      buffer.func_225582_a_(length, width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, -width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, -width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, -width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, -width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, -width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, -width, width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(0, -width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
      buffer.func_225582_a_(length, -width, -width).func_225587_b_(0, 0).func_227885_a_(r, g, b, alpha).endVertex();
    }
    tessy.draw();
    //    matrixStack.func_227865_b_(); // pop
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
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, -z_size + z).func_225583_a_(maxU, maxV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, z_size + z).func_225583_a_(maxU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, z_size + z).func_225583_a_(minU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, -z_size + z).func_225583_a_(minU, maxV).endVertex();
    // DOWN
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, z_size + z).func_225583_a_(minU, minV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, -z_size + z).func_225583_a_(minU, maxV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, -z_size + z).func_225583_a_(maxU, maxV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, z_size + z).func_225583_a_(maxU, minV).endVertex();
    // LEFT
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, z_size + z).func_225583_a_(maxU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, -z_size + z).func_225583_a_(maxU, maxV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, -z_size + z).func_225583_a_(minU, maxV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, z_size + z).func_225583_a_(minU, minV).endVertex();
    // RIGHT
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, -z_size + z).func_225583_a_(minU, maxV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, z_size + z).func_225583_a_(minU, minV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, z_size + z).func_225583_a_(maxU, minV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, -z_size + z).func_225583_a_(maxU, maxV).endVertex();
    // BACK
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, -z_size + z).func_225583_a_(minU, maxV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, -z_size + z).func_225583_a_(minU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, -z_size + z).func_225583_a_(maxU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, -z_size + z).func_225583_a_(maxU, maxV).endVertex();
    // FRONT
    bufferbuilder.func_225582_a_(x_size + x, -y_size + y, z_size + z).func_225583_a_(maxU, minV).endVertex();
    bufferbuilder.func_225582_a_(x_size + x, y_size + y, z_size + z).func_225583_a_(maxU, maxV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, y_size + y, z_size + z).func_225583_a_(minU, maxV).endVertex();
    bufferbuilder.func_225582_a_(-x_size + x, -y_size + y, z_size + z).func_225583_a_(minU, minV).endVertex();
  }
}
