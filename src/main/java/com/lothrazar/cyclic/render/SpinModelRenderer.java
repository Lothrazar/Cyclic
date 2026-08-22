package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.ModCyclic;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public final class SpinModelRenderer {

  public static final StandaloneModelKey<QuadCollection> SPRINKLER_SPIN = new StandaloneModelKey<>(() -> ModCyclic.MODID + ":block/sprinkler_spin");
  public static final StandaloneModelKey<QuadCollection> FOUNTAIN_SPIN = new StandaloneModelKey<>(() -> ModCyclic.MODID + ":block/experience_fountain_spin");

  private SpinModelRenderer() {
  }

  public static void render(StandaloneModelKey<QuadCollection> key, float angleDeg, PoseStack matrix,
                            MultiBufferSource buffers, int light, int overlay) {
    QuadCollection model = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
    if (model == null) {
      return;
    }
    RenderType renderType = RenderTypes.itemCutout(TextureAtlas.LOCATION_BLOCKS);
    VertexConsumer vc = buffers.getBuffer(renderType);
    matrix.pushPose();
    matrix.translate(0.5D, 0D, 0.5D);
    matrix.mulPose(Axis.YP.rotationDegrees(angleDeg));
    matrix.translate(-0.5D, 0D, -0.5D);
    PoseStack.Pose pose = matrix.last();
    QuadInstance instance = new QuadInstance();
    instance.setColor(-1);
    instance.setLightCoords(light);
    instance.setOverlayCoords(overlay);
    for (BakedQuad q : model.getAll()) {
      vc.putBakedQuad(pose, q, instance);
    }
    matrix.popPose();
  }
}
