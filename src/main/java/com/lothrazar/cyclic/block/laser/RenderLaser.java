package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

/**
 * laser rendering by direwolf20-MC from this MIT project
 * <p>
 * <p>
 * https://github.com/Direwolf20-MC/DireGoo2/blob/master/LICENSE.md
 */
public class RenderLaser implements BlockEntityRenderer<TileLaser> {

  public RenderLaser(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileLaser te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.requiresRedstone() && !te.isPowered()) {
      return;
    }
    try {
      draw(te, matrixStack, iRenderTypeBuffer);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("RenderLaser.java ", e);
    }
  }

  public static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, BlockPos tile) {
    //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
    Player player = Minecraft.getInstance().player;
    Vector3f vectP = new Vector3f((float) player.getX() - tile.getX(), (float) player.getEyeY() - tile.getY(), (float) player.getZ() - tile.getZ());
    Vector3f vectS = from.copy();
    vectS.sub(vectP);
    Vector3f vectE = to.copy();
    vectE.sub(from);
    Vector3f adjustedVec = vectS.copy();
    adjustedVec.cross(vectE);
    adjustedVec.normalize();
    return adjustedVec;
  }

  public static void draw(TileLaser tile, PoseStack matrixStackIn, MultiBufferSource bufferIn) throws Exception {
    BlockPos posTarget = tile.getPosTarget();
    if (posTarget == null || posTarget.equals(BlockPos.ZERO)) {
      return;
    }
    //now render
    matrixStackIn.pushPose();
    Matrix4f positionMatrix = matrixStackIn.last().pose();
    //diff between target and tile, targets always centered
    BlockPos tilePos = tile.getBlockPos();
    Vector3f from = new Vector3f(
        posTarget.getX() + .5F - tilePos.getX(),
        posTarget.getY() + .5F - tilePos.getY(),
        posTarget.getZ() + .5F - tilePos.getZ());
    Vector3f to = new Vector3f(tile.xOffset.getOffset(), tile.yOffset.getOffset(), tile.zOffset.getOffset());
    VertexConsumer builder = bufferIn.getBuffer(FakeBlockRenderTypes.LASER_MAIN_BEAM);
    drawDirewolfLaser(builder, positionMatrix, from, to, tile.getRed(), tile.getGreen(), tile.getBlue(), tile.getAlpha(), tile.getThick(), tilePos);
    final float coreThick = 0.01F;
    drawDirewolfLaser(builder, positionMatrix, from, to, 1, 1, 1, tile.getAlpha(), coreThick, tilePos);
    matrixStackIn.popPose();
  }

  public static void drawDirewolfLaser(VertexConsumer builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, BlockPos tilePos) {
    final float v = 1;
    Vector3f adjustedVec = adjustBeamToEyes(from, to, tilePos);
    adjustedVec.mul(thickness); //Determines how thick the beam is
    Vector3f p1 = from.copy();
    p1.add(adjustedVec);
    Vector3f p2 = from.copy();
    p2.sub(adjustedVec);
    Vector3f p3 = to.copy();
    p3.add(adjustedVec);
    Vector3f p4 = to.copy();
    p4.sub(adjustedVec);
    builder.vertex(positionMatrix, p1.x(), p1.y(), p1.z())
        .color(r, g, b, alpha)
        .uv(1, v)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(15728880)
        .endVertex();
    builder.vertex(positionMatrix, p3.x(), p3.y(), p3.z())
        .color(r, g, b, alpha)
        .uv(1, v)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(15728880)
        .endVertex();
    builder.vertex(positionMatrix, p4.x(), p4.y(), p4.z())
        .color(r, g, b, alpha)
        .uv(0, v)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(15728880)
        .endVertex();
    builder.vertex(positionMatrix, p2.x(), p2.y(), p2.z())
        .color(r, g, b, alpha)
        .uv(0, v)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(15728880)
        .endVertex();
  }

  @Override
  public boolean shouldRenderOffScreen(TileLaser te) {
    return true;
  }
}
