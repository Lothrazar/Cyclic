package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderDropper implements BlockEntityRenderer<TileDropper> {

  public RenderDropper(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileDropper te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDropper.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.5F, ClientConfigCyclic.getColor(te));
    }
  }
}
