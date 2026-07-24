package com.lothrazar.cyclic.item.boomerang;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class BoomerangRenderer<T extends BoomerangEntity> extends EntityRenderer<T, BoomerangRenderer.State> {

  private static final float SPIN_DEGREES_PER_TICK = 30.0F;
  private final ItemModelResolver itemModelResolver;

  public static class State extends EntityRenderState {
    ItemStackRenderState item = new ItemStackRenderState();
  }

  public BoomerangRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.itemModelResolver = context.getItemModelResolver();
    this.shadowRadius = 0.0F;
    this.shadowStrength = 0.0F;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(T entity, State state, float partialTicks) {
    super.extractRenderState(entity, state, partialTicks);
    this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.FIXED, entity);
  }

  @Override
  public void submit(State state, PoseStack ms, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    ms.pushPose();
    float spin = state.ageInTicks * SPIN_DEGREES_PER_TICK;
    // outer: spin around world Y (horizontal spin like a real boomerang)
    ms.mulPose(Axis.YP.rotationDegrees(spin));
    // inner: tip the sprite flat so the face points up instead of at the camera
    ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
    state.item.submit(ms, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
    ms.popPose();
    super.submit(state, ms, submitNodeCollector, camera);
  }
}
