package com.lothrazar.cyclic.block.detectorentity;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderDetector implements BlockEntityRenderer<TileDetector> {

  public RenderDetector(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileDetector te, float v, PoseStack matrix, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileDetector.Fields.RENDER.ordinal());
    if (previewType == PreviewOutlineType.SHADOW.ordinal()) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrix, 0.9F, ClientConfigCyclic.getColor(te));
    }
    if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        RenderBlockUtils.createBox(matrix, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
