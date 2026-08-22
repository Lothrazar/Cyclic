package com.lothrazar.cyclic.block.conveyor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;

public class ConveyorItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<ConveyorItemEntity, ConveyorItemRenderer.State> {

  private final ItemModelResolver itemModelResolver;

  public static class State extends EntityRenderState {
    ItemStackRenderState item = new ItemStackRenderState();
  }

  public ConveyorItemRenderer(EntityRendererProvider.Context renderManager) {
    super(renderManager);
    this.itemModelResolver = renderManager.getItemModelResolver();
    this.shadowRadius = 0.0F;
    this.shadowStrength = 0.0F;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(ConveyorItemEntity entity, State state, float partialTicks) {
    super.extractRenderState(entity, state, partialTicks);
    // FIXED so the sprite/model faces the camera before submit() decides on rotation based on
    // whether the resolved model turned out to be a 3D block model or a flat 2D item sprite.
    this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.FIXED, entity);
  }

  @Override
  public void submit(State state, PoseStack ms, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    ms.pushPose();
    boolean isGui3d = state.item.getModelBoundingBox().getZsize() > 0.1;
    if (isGui3d) {
      // 3D block models render naturally with no rotation; our renderer never calls the vanilla
      // bob/spin logic so they remain still on the belt.
    }
    else {
      ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
    }
    state.item.submit(ms, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
    ms.popPose();
    super.submit(state, ms, submitNodeCollector, camera);
  }

  @Override
  public boolean shouldRender(ConveyorItemEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
    return true;
  }
}
