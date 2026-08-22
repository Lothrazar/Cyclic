package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RenderHarvester implements BlockEntityRenderer<TileHarvester, RenderHarvester.State> {

  public static class State extends BlockEntityRenderState {
    TileHarvester blockEntity;
  }

  public RenderHarvester(BlockEntityRendererProvider.Context d) {
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileHarvester blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileHarvester te = state.blockEntity;
    int previewType = te.getField(TileHarvester.Fields.RENDER.ordinal());
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
