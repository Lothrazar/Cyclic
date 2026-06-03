package com.lothrazar.cyclic.render.beacon;

import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

/**
 * Shared renderer base for the Cyclic beacon family. The concrete renderer
 * only needs to declare its tile type as the type argument; activation is
 * gated by {@link BeamHolder#isBeamActive()} and the actual beam draw is
 * delegated to {@link BeaconBeamRenderer}.
 *
 * Per-color rendering is driven by {@link BeaconBlockEntity.BeaconBeamSection#getColor()},
 * which {@link BeamHolder#updateBeam} populates from {@code BeaconBeamBlock}s
 * in the column (vanilla stained glass etc.).
 */
public abstract class RenderBeaconBase<T extends BlockEntity & BeamHolder> implements BlockEntityRenderer<T> {

  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    return AABB.INFINITE;
  }

  @Override
  public int getViewDistance() {
    return 256;
  }

  @Override
  public void render(T tile, float partial, PoseStack ms, MultiBufferSource buf, int light, int overlay) {
    if (!tile.isBeamActive()) {
      return;
    }
    long time = tile.getLevel().getGameTime();
    List<BeaconBlockEntity.BeaconBeamSection> sections = tile.getBeamSections();
    int yOffset = 0;
    for (int k = 0; k < sections.size(); ++k) {
      BeaconBlockEntity.BeaconBeamSection section = sections.get(k);
      int height = (k == sections.size() - 1) ? 1024 : section.getHeight();
      BeaconBeamRenderer.drawBeam(ms, buf, partial, time, yOffset, height, section.getColor());
      yOffset += section.getHeight();
    }
  }
}
