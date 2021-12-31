package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderItemCollect implements BlockEntityRenderer<TileItemCollector> {

  public RenderItemCollect(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileItemCollector te, float v, PoseStack matrix,
      MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    if (1 == te.getField(TileItemCollector.Fields.RENDER.ordinal())) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShape(), matrix, 0.5F, ClientConfigCyclic.getColor(te));
    }
  }
}
