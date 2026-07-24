package com.lothrazar.cyclic.block.wireless.energy;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
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

public class RenderWirelessEnergy implements BlockEntityRenderer<TileWirelessEnergy, RenderWirelessEnergy.State> {

  public static class State extends BlockEntityRenderState {
    TileWirelessEnergy blockEntity;
  }

  public RenderWirelessEnergy(BlockEntityRendererProvider.Context d) {}

  @Override
  public boolean shouldRenderOffScreen() {
    return true;
  }

  @Override
  public State createRenderState() {
    return new State();
  }

  @Override
  public void extractRenderState(TileWirelessEnergy blockEntity, State state, float partialTicks, Vec3 cameraPosition,
      ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrix, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileWirelessEnergy te = state.blockEntity;
    int previewType = te.getField(TileWirelessEnergy.Fields.RENDER.ordinal());
    if (previewType <= 0) {
      return;
    }
    List<BlockPos> shape = new ArrayList<>();
    String dimensionId = LevelWorldUtil.dimensionToString(te.getLevel());
    for (int slot = 0; slot < te.gpsSlots.getSlots(); slot++) {
      BlockPosDim target = te.getTargetInSlot(slot);
      if (target != null && target.getDimension().equalsIgnoreCase(dimensionId)) {
        shape.add(target.getPos());
      }
    }
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), shape, matrix, 0.9F, ClientConfigCyclic.getColor(te));
    } else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : shape) {
        RenderBlockUtils.createBox(matrix, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
