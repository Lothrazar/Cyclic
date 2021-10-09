package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderFluidCollect implements BlockEntityRenderer<TileFluidCollect> {

  public RenderFluidCollect(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileFluidCollect te, float v, PoseStack matrix, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    if (1 == te.getField(TileFluidCollect.Fields.RENDER.ordinal())) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShapeHollow(), matrix, 0.4F, ClientConfigCyclic.getColor(te));
    }
  }
}
