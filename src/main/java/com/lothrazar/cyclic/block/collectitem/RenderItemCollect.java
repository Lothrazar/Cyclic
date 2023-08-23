package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderItemCollect implements BlockEntityRenderer<TileItemCollector> {

  public RenderItemCollect(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileItemCollector te, float v, PoseStack matrix,
      MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileItemCollector.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), te.getShape(), matrix, 0.9F, ClientConfigCyclic.getColor(te));
    }
    if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShape()) {
        RenderBlockUtils.createBox(matrix, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
