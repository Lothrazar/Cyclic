package com.lothrazar.cyclic.block.solidifier;

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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jspecify.annotations.Nullable;

public class RenderSolidifier implements BlockEntityRenderer<TileSolidifier, RenderSolidifier.State> {

  public static class State extends BlockEntityRenderState {
    TileSolidifier blockEntity;
    ItemStackRenderState item0;
    ItemStackRenderState item1;
    ItemStackRenderState item2;
  }

  private final ItemModelResolver itemModelResolver;

  public RenderSolidifier(BlockEntityRendererProvider.Context d) {
    this.itemModelResolver = d.itemModelResolver();
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileSolidifier tankHere, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(tankHere, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = tankHere;
    IItemHandler itemHandler = CapabilityUtil.item(tankHere.getLevel(), tankHere.getBlockPos());
    if (itemHandler != null) {
      var level = tankHere.getLevel();
      ItemStack s0 = itemHandler.getStackInSlot(0);
      if (!s0.isEmpty()) {
        ItemStackRenderState st = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(st, s0, ItemDisplayContext.GROUND, level, null, 0x111111);
        state.item0 = st;
      }
      ItemStack s1 = itemHandler.getStackInSlot(1);
      if (!s1.isEmpty()) {
        ItemStackRenderState st = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(st, s1, ItemDisplayContext.GROUND, level, null, 0x777777);
        state.item1 = st;
      }
      ItemStack s2 = itemHandler.getStackInSlot(2);
      if (!s2.isEmpty()) {
        ItemStackRenderState st = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(st, s2, ItemDisplayContext.GROUND, level, null, 0xBBBBBB);
        state.item2 = st;
      }
    }
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileSolidifier tankHere = state.blockEntity;
    int light = state.lightCoords;
    if (state.item0 != null) {
      matrixStack.pushPose();
      //        matrixStack.scale(0.5f, 0.5f, 0.5f);
      matrixStack.translate(0.4, 0.65, 0.4);
      state.item0.submit(matrixStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
      matrixStack.popPose();
    }
    if (state.item1 != null) {
      matrixStack.pushPose();
      //        matrixStack.scale(0.5f, 0.5f, 0.5f);
      matrixStack.translate(0.5, 0.45, 0.5);
      state.item1.submit(matrixStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
      matrixStack.popPose();
    }
    if (state.item2 != null) {
      matrixStack.pushPose();
      matrixStack.translate(0.6, 0.15, 0.6);
      state.item2.submit(matrixStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
      matrixStack.popPose();
    }
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(), tankHere.getBlockPos());
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
    VertexConsumer vertexBuffer = buffer.getBuffer(FluidTankRenderType.RESIZABLE);
    matrixStack.pushPose();
    matrixStack.scale(1F, FluidHelpers.getScale(tankHere.tank), 1F);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrixStack, vertexBuffer, RenderBlockUtils.getColorARGB(fluid),
        RenderBlockUtils.calculateGlowLight(light, fluid));
    matrixStack.popPose();
    buffer.endBatch();
  }
}
