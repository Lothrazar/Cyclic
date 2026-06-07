package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.ModCyclic;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public final class SpinModelRenderer {

  public static final ModelResourceLocation SPRINKLER_SPIN = ModelResourceLocation.standalone(
      ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/sprinkler_spin"));
  public static final ModelResourceLocation FOUNTAIN_SPIN = ModelResourceLocation.standalone(
      ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block/experience_fountain_spin"));

  private static final RandomSource RANDOM = RandomSource.create();

  private SpinModelRenderer() {}

  public static void render(ModelResourceLocation key, float angleDeg, PoseStack matrix,
      MultiBufferSource buffers, int light, int overlay) {
    BakedModel model = Minecraft.getInstance().getModelManager().getModel(key);
    if (model == null) {
      return;
    }
    VertexConsumer vc = buffers.getBuffer(RenderType.cutout());
    matrix.pushPose();
    matrix.translate(0.5D, 0D, 0.5D);
    matrix.mulPose(Axis.YP.rotationDegrees(angleDeg));
    matrix.translate(-0.5D, 0D, -0.5D);
    PoseStack.Pose pose = matrix.last();
    for (BakedQuad q : model.getQuads(null, null, RANDOM)) {
      vc.putBulkData(pose, q, 1f, 1f, 1f, 1f, light, overlay);
    }
    for (Direction d : Direction.values()) {
      for (BakedQuad q : model.getQuads(null, d, RANDOM)) {
        vc.putBulkData(pose, q, 1f, 1f, 1f, 1f, light, overlay);
      }
    }
    matrix.popPose();
  }
}
