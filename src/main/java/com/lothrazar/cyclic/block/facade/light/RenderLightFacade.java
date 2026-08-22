package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.library.util.FacadeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RenderLightFacade implements BlockEntityRenderer<TileLightFacade, RenderLightFacade.State> {

  public static class State extends BlockEntityRenderState {
    TileLightFacade blockEntity;
  }

  public RenderLightFacade(BlockEntityRendererProvider.Context d) {
  }

  @Override
  public boolean shouldRenderOffScreen() {
    return true;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileLightFacade blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileLightFacade te = state.blockEntity;
    BlockState facadeState = te.getFacadeState(te.getLevel());
    MultiBufferSource.BufferSource ibuffer = Minecraft.getInstance().renderBuffers().bufferSource();
    FacadeUtil.renderBlockState(te.getLevel(), te.getBlockPos(), ibuffer, matrixStack, facadeState, state.lightCoords, 0);
    ibuffer.endBatch();
  }
}
