package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderPeatFarm implements BlockEntityRenderer<TilePeatFarm> {

  public RenderPeatFarm(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TilePeatFarm te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TilePeatFarm.Fields.RENDER.ordinal());
    if (previewType == PreviewOutlineType.SHADOW.ordinal()) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.9F, ClientConfigCyclic.getColor(te));
    }
    if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        RenderBlockUtils.createBox(matrixStack, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
