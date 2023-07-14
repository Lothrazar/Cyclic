package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderForester implements BlockEntityRenderer<TileForester> {

  public RenderForester(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileForester te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    // ok
    if (te.getField(TileForester.Fields.RENDER.ordinal()) == 1) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShapeHollow(), matrixStack, 0.5F, ClientConfigCyclic.getColor(te));
    }
  }
}
