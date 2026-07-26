package com.lothrazar.cyclic.render.beacon;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Shared renderer base for the Cyclic beacon family. The concrete renderer
 * only needs to declare its tile type as the type argument; activation is
 * gated by {@link BeamHolder#isBeamActive()}. The actual beam draw delegates
 * to vanilla's own {@link BeaconRenderer#submitBeaconBeam}, since 26.1's
 * beam rendering moved onto the SubmitNodeCollector deferred-submission
 * pipeline and vanilla already exposes that logic as a public static helper.
 *
 * Per-color rendering is driven by {@link BeaconBeamOwner.Section#getColor()},
 * which {@link BeamHolder#updateBeam} populates from {@code BeaconBeamBlock}s
 * in the column (vanilla stained glass etc.).
 */
public abstract class RenderBeaconBase<T extends BlockEntity & BeamHolder> implements BlockEntityRenderer<T, RenderBeaconBase.State> {

  public static class State extends BlockEntityRenderState {
    BlockEntity blockEntity;
    float animationTime;
    List<BeaconBeamOwner.Section> sections;
  }

  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    return AABB.INFINITE;
  }

  @Override
  public int getViewDistance() {
    return 256;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(T tile, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = tile;
    if (tile.isBeamActive() && tile.getLevel() != null) {
      state.animationTime = Math.floorMod(tile.getLevel().getGameTime(), 40) + partialTicks;
      state.sections = tile.getBeamSections();
    }
    else {
      state.sections = List.of();
    }
  }

  @Override
  public void submit(State state, PoseStack ms, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    if (state.sections.isEmpty()) {
      return;
    }
    int yOffset = 0;
    for (int k = 0; k < state.sections.size(); ++k) {
      BeaconBeamOwner.Section section = state.sections.get(k);
      int height = (k == state.sections.size() - 1) ? 1024 : section.getHeight();
      BeaconRenderer.submitBeaconBeam(ms, submitNodeCollector, BeaconRenderer.BEAM_LOCATION, 1.0F, state.animationTime, yOffset, height,
          section.getColor(), 0.2F, 0.25F);
      yOffset += section.getHeight();
    }
  }
}
