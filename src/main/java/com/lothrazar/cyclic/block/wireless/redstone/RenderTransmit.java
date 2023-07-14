package com.lothrazar.cyclic.block.wireless.redstone;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.lothrazar.cyclic.block.laser.RenderLaser;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;

public class RenderTransmit implements BlockEntityRenderer<TileWirelessTransmit> {

  public RenderTransmit(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileWirelessTransmit te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.requiresRedstone() && !te.isPowered()) {
      return;
    }
    if (te.getField(TileWirelessTransmit.Fields.RENDER.ordinal()) < 1) {
      return;
    }
    for (int slot = 0; slot < te.inventory.getSlots(); slot++) {
      draw(slot, te, matrixStack, iRenderTypeBuffer);
    }
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
  public boolean shouldRenderOffScreen(TileWirelessTransmit te) {
    return true;
  }
}
