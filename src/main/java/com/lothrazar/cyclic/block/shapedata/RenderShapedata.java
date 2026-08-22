package com.lothrazar.cyclic.block.shapedata;

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

import java.awt.*;

public class RenderShapedata implements BlockEntityRenderer<TileShapedata, RenderShapedata.State> {

  public static class State extends BlockEntityRenderState {
    TileShapedata blockEntity;
  }

  public RenderShapedata(BlockEntityRendererProvider.Context d) {
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileShapedata blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileShapedata te = state.blockEntity;
    int previewType = te.getField(TileShapedata.Fields.RENDER.ordinal());
    if (PreviewOutlineType.NONE.ordinal() == previewType) {
      return;
    }
    BlockPos targetA = te.getTarget(0);
    BlockPos targetB = te.getTarget(1);
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      if (targetA != null) {
        RenderBlockUtils.renderOutline(te.getBlockPos(), targetA, matrixStack, 1.05F, Color.BLUE);
      }
      if (targetB != null) {
        RenderBlockUtils.renderOutline(te.getBlockPos(), targetB, matrixStack, 1.05F, Color.RED);
      }
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      if (targetA != null) {
        RenderBlockUtils.createBox(matrixStack, targetA, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
      if (targetB != null) {
        RenderBlockUtils.createBox(matrixStack, targetB, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
