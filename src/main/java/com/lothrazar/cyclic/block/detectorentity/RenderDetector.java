package com.lothrazar.cyclic.block.detectorentity;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.render.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderDetector implements BlockEntityRenderer<TileDetector> {

  public RenderDetector(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileDetector te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDetector.Fields.RENDER.ordinal()) == 1) {
      RenderUtils.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.6F, ClientConfigCyclic.getColor(te));
    }
  }
}
