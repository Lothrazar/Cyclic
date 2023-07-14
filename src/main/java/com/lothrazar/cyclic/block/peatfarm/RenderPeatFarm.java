package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderPeatFarm implements BlockEntityRenderer<TilePeatFarm> {

  public RenderPeatFarm(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TilePeatFarm te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TilePeatFarm.Fields.RENDER.ordinal()) == 1) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.4F, ClientConfigCyclic.getColor(te));
    }
  }
}
