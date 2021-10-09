package com.lothrazar.cyclic.block.miner;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderMiner implements BlockEntityRenderer<TileMiner> {

  public RenderMiner(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileMiner te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileMiner.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShapeHollow(), matrixStack, 0.4F, ClientConfigCyclic.getColor(te));
    }
  }
}
