package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class RenderPeatFarm extends BlockEntityRenderer<TilePeatFarm> {

  public RenderPeatFarm(BlockEntityRenderDispatcher d) {
    super(d);
  }

  @Override
  public void render(TilePeatFarm te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TilePeatFarm.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.4F, ClientConfigCyclic.getColor(te));
    }
  }
}
