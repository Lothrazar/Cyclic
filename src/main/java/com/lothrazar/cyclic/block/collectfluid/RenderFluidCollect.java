package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderFluidCollect implements BlockEntityRenderer<TileFluidCollect> {

  public RenderFluidCollect(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileFluidCollect te, float v, PoseStack matrix, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileFluidCollect.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShapeHollow(), matrix, 0.9F, ClientConfigCyclic.getColor(te));
    }
    if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShapeHollow()) {
        RenderBlockUtils.createBox(matrix, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
