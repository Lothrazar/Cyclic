package com.lothrazar.cyclic.render.beacon;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

/**
 * Pure beam-draw helper. A 1.20-era copy of vanilla's
 * {@link BeaconRenderer#renderBeaconBeam} that the Cyclic beacons share, since
 * vanilla's helper is tightly coupled to {@link BeaconBlockEntity} and can't
 * be reused directly.
 *
 * The only entry point client code should call is
 * {@link #drawBeam(PoseStack, MultiBufferSource, float, long, int, int, int)},
 * which takes the ARGB color stored on a {@link BeaconBlockEntity.BeaconBeamSection}
 * and handles the float-component conversion internally.
 *
 * The lower-level overloads are kept public for callers that want to draw a
 * beam with non-default radius or with a custom beam texture.
 */
public final class BeaconBeamRenderer {

  private BeaconBeamRenderer() {}

  /**
   * Draw one segment of a beam at the default vanilla beam radius.
   *
   * @param ms         pose stack from the BER render method
   * @param buf        buffer source from the BER render method
   * @param partial    partial tick from the BER render method
   * @param time       {@code level.getGameTime()}
   * @param yOffset    distance above the beacon at which this segment starts
   * @param height     length of this segment in blocks; pass a large value
   *                   (e.g. 1024) for the topmost segment to extend out of sight
   * @param argbColor  beam tint as ARGB int (see
   *                   {@link BeaconBlockEntity.BeaconBeamSection#getColor()})
   */
  public static void drawBeam(PoseStack ms, MultiBufferSource buf, float partial,
                              long time, int yOffset, int height, int argbColor) {
    float[] rgb = argbToFloats(argbColor);
    renderBeaconBeam(ms, buf, BeaconRenderer.BEAM_LOCATION, partial, 1.0F, time, yOffset, height, rgb, 0.2F, 0.25F);
  }

  /**
   * Convert an ARGB int to the {@code float[3]} tint the beam shader expects.
   * Alpha is dropped; the beam is always opaque.
   */
  public static float[] argbToFloats(int argb) {
    return new float[] {
        FastColor.ARGB32.red(argb) / 255.0F,
        FastColor.ARGB32.green(argb) / 255.0F,
        FastColor.ARGB32.blue(argb) / 255.0F
    };
  }

  public static void renderBeaconBeam(PoseStack ms, MultiBufferSource buf, float partial,
                                      long time, int yOffset, int height, float[] rgb) {
    renderBeaconBeam(ms, buf, BeaconRenderer.BEAM_LOCATION, partial, 1.0F, time, yOffset, height, rgb, 0.2F, 0.25F);
  }

  public static void renderBeaconBeam(PoseStack ms, MultiBufferSource buf, Identifier rl,
                                      float partial, float textureScale, long time,
                                      int yOffset, int height, float[] rgb,
                                      float innerRadius, float outerRadius) {
    int top = yOffset + height;
    ms.pushPose();
    ms.translate(0.5D, 0.0D, 0.5D);
    float f = Math.floorMod(time, 40) + partial;
    float f1 = height < 0 ? f : -f;
    float f2 = Mth.frac(f1 * 0.2F - Mth.floor(f1 * 0.1F));
    float r = rgb[0];
    float g = rgb[1];
    float b = rgb[2];
    ms.pushPose();
    ms.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
    float f9 = -innerRadius;
    float f12 = -innerRadius;
    float f15 = -1.0F + f2;
    float f16 = height * textureScale * (0.5F / innerRadius) + f15;
    renderPart(ms, buf.getBuffer(RenderTypes.beaconBeam(rl, false)), r, g, b, 1.0F, yOffset, top,
        0.0F, innerRadius, innerRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
    ms.popPose();
    float f6 = -outerRadius;
    float f7 = -outerRadius;
    float f8 = -outerRadius;
    f9 = -outerRadius;
    f15 = -1.0F + f2;
    f16 = height * textureScale + f15;
    renderPart(ms, buf.getBuffer(RenderTypes.beaconBeam(rl, true)), r, g, b, 0.125F, yOffset, top,
        f6, f7, outerRadius, f8, f9, outerRadius, outerRadius, outerRadius, 0.0F, 1.0F, f16, f15);
    ms.popPose();
  }

  public static void renderPart(PoseStack ms, VertexConsumer vc,
                                float r, float g, float b, float a, int yBottom, int yTop,
                                float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
                                float u0, float u1, float vTop, float vBottom) {
    PoseStack.Pose pose = ms.last();
    Matrix4f m4 = pose.pose();
    Matrix3f m3 = pose.normal();
    renderQuad(m4, m3, vc, r, g, b, a, yBottom, yTop, x1, z1, x2, z2, u0, u1, vTop, vBottom);
    renderQuad(m4, m3, vc, r, g, b, a, yBottom, yTop, x4, z4, x3, z3, u0, u1, vTop, vBottom);
    renderQuad(m4, m3, vc, r, g, b, a, yBottom, yTop, x2, z2, x4, z4, u0, u1, vTop, vBottom);
    renderQuad(m4, m3, vc, r, g, b, a, yBottom, yTop, x3, z3, x1, z1, u0, u1, vTop, vBottom);
  }

  public static void renderQuad(Matrix4f m4, Matrix3f m3, VertexConsumer vc,
                                float r, float g, float b, float a, int yBottom, int yTop,
                                float x1, float z1, float x2, float z2,
                                float u0, float u1, float vTop, float vBottom) {
    addVertex(m4, m3, vc, r, g, b, a, yTop, x1, z1, u1, vTop);
    addVertex(m4, m3, vc, r, g, b, a, yBottom, x1, z1, u1, vBottom);
    addVertex(m4, m3, vc, r, g, b, a, yBottom, x2, z2, u0, vBottom);
    addVertex(m4, m3, vc, r, g, b, a, yTop, x2, z2, u0, vTop);
  }

  public static void addVertex(Matrix4f m4, Matrix3f m3, VertexConsumer vc,
                               float r, float g, float b, float a, int y,
                               float x, float z, float u, float v) {
    vc.addVertex(m4, x, y, z)
        .setColor(r, g, b, a)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(15728880)
        .setNormal(0.0F, 1.0F, 0.0F);
  }
}
