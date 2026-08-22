package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.block.laser.RenderLaser;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.render.type.FakeBlockRenderTypes;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RenderTransmit implements BlockEntityRenderer<TileWirelessTransmit, RenderTransmit.State> {

  public static class State extends BlockEntityRenderState {
    TileWirelessTransmit blockEntity;
  }

  public RenderTransmit(BlockEntityRendererProvider.Context d) {
  }

  public static void draw(int slot, TileWirelessTransmit tile, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
    BlockPosDim posPosTarget = tile.getTargetInSlot(slot);
    if (posPosTarget == null) {
      return;
    }
    if (!LevelWorldUtil.dimensionIsEqual(posPosTarget, tile.getLevel())) {
      return;//its crossing dimensions, skip renderono
    }
    BlockPos posTarget = posPosTarget.getPos();
    if (posTarget == null || posTarget.equals(BlockPos.ZERO)) {
      return;
    }
    //    //now render
    matrixStackIn.pushPose();
    Matrix4f positionMatrix = matrixStackIn.last().pose();
    //diff between target and tile, targets always centered
    BlockPos tilePos = tile.getBlockPos();
    Vector3f from = new Vector3f(
        posTarget.getX() + .5F - tilePos.getX(),
        posTarget.getY() + .5F - tilePos.getY(),
        posTarget.getZ() + .5F - tilePos.getZ());
    Vector3f to = new Vector3f(.5F, .5F, .5F);
    VertexConsumer builder = bufferIn.getBuffer(FakeBlockRenderTypes.LASER_MAIN_BEAM);
    RenderLaser.drawDirewolfLaser(builder, positionMatrix, from, to, tile.getRed(), tile.getGreen(), tile.getBlue(), tile.getAlpha(), tile.getThick(), tilePos);
    final float coreThick = 0.01F;
    RenderLaser.drawDirewolfLaser(builder, positionMatrix, from, to, 1, 1, 1, tile.getAlpha(), coreThick, tilePos);
    matrixStackIn.popPose();
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
  public void extractRenderState(TileWirelessTransmit blockEntity, State state, float partialTicks, Vec3 cameraPosition,
                                 ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    state.blockEntity = blockEntity;
  }

  @Override
  public void submit(State state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    TileWirelessTransmit te = state.blockEntity;
    if (te.requiresRedstone() && !te.isPowered()) {
      return;
    }
    int previewType = te.getField(TileWirelessTransmit.Fields.RENDER.ordinal());
    if (previewType <= 0) {
      return;
    }
    List<BlockPos> shape = new ArrayList<>();
    String dimensionId = LevelWorldUtil.dimensionToString(te.getLevel());
    for (int slot = 0; slot < te.inventory.getSlots(); slot++) {
      BlockPosDim dimPosSaved = te.getTargetInSlot(slot);
      if (dimPosSaved != null
          && dimPosSaved.getDimension().equalsIgnoreCase(dimensionId)) {
        shape.add(dimPosSaved.getPos());
      }
      //        draw(slot, te, matrixStack, iRenderTypeBuffer);
    }
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      RenderBlockUtils.renderOutline(te.getBlockPos(), shape, matrixStack, 0.9F, ClientConfigCyclic.getColor(te));
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : shape) {
        RenderBlockUtils.createBox(matrixStack, crd, Vec3.atLowerCornerOf(te.getBlockPos()));
      }
    }
  }
}
