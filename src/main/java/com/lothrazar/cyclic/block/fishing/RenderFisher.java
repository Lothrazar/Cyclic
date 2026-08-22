package com.lothrazar.cyclic.block.fishing;

import com.mojang.blaze3d.vertex.PoseStack;
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
import org.jspecify.annotations.Nullable;

public class RenderFisher implements BlockEntityRenderer<TileFisher, RenderFisher.State> {

  public static class State extends BlockEntityRenderState {
    ItemStackRenderState item;
  }

  private final ItemModelResolver itemModelResolver;

  public RenderFisher(BlockEntityRendererProvider.Context d) {
    this.itemModelResolver = d.itemModelResolver();
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileFisher tankHere, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(tankHere, state, partialTicks, cameraPosition, breakProgress);
    if (tankHere.inventory != null) {
      ItemStack stack = tankHere.inventory.getStackInSlot(0);
      if (!stack.isEmpty()) {
        ItemStackRenderState itemState = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.GROUND, tankHere.getLevel(), null, 0x910911);
        state.item = itemState;
      }
    }
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    if (state.item != null) {
      matrixStack.pushPose();
      //        matrixStack.scale(0.5f, 0.5f, 0.5f);
      matrixStack.translate(0.4, 0.45, 0.4);
      state.item.submit(matrixStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
      matrixStack.popPose();
    }
  }
}
