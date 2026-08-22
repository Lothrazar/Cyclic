package com.lothrazar.cyclic.block.expfountain;

import com.lothrazar.cyclic.render.SpinModelRenderer;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.render.type.FluidTankRenderType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jspecify.annotations.Nullable;

public class RenderExperienceFountain implements BlockEntityRenderer<TileExperienceFountain, RenderExperienceFountain.State> {

  public static class State extends BlockEntityRenderState {
    TileExperienceFountain blockEntity;
    float partialTicks;
  }

  public RenderExperienceFountain(BlockEntityRendererProvider.Context d) {
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileExperienceFountain blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
    state.partialTicks = partialTicks;
  }

  @Override
  public void submit(State state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileExperienceFountain tankHere = state.blockEntity;
    float v = state.partialTicks;
    int light = state.lightCoords;
    MultiBufferSource.BufferSource renderer = Minecraft.getInstance().renderBuffers().bufferSource();
    float angle = 0F;
    if (tankHere.getLevel() != null && tankHere.getLevel().hasNeighborSignal(tankHere.getBlockPos())) {
      angle = ((tankHere.getLevel().getGameTime() % 60L) + v) * 6F;
    }
    SpinModelRenderer.render(SpinModelRenderer.FOUNTAIN_SPIN, angle, matrix, renderer, light, 0);
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(), tankHere.getBlockPos());
    if (handler == null || handler.getFluidInTank(0) == null) {
      renderer.endBatch();
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      renderer.endBatch();
      return;
    }
    VertexConsumer buffer = renderer.getBuffer(FluidTankRenderType.RESIZABLE);
    matrix.scale(1F, FluidHelpers.getScale(tankHere.tank) / 4F, 1F);
    float f = 0.5F;
    matrix.scale(f, 1F, f);
    matrix.translate(f, 0, f);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrix, buffer, RenderBlockUtils.getColorARGB(fluid),
        RenderBlockUtils.calculateGlowLight(light, fluid));
    renderer.endBatch();
  }
}
