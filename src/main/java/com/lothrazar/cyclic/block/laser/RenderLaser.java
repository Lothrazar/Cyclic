package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * laser rendering by direwolf20-MC from this MIT project
 * 
 * 
 * https://github.com/Direwolf20-MC/DireGoo2/blob/master/LICENSE.md
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderLaser extends TileEntityRenderer<TileLaser> {

  public RenderLaser(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileLaser te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
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

  private static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, BlockPos tile) {
    //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
    PlayerEntity player = Minecraft.getInstance().player;
    Vector3f P = new Vector3f((float) player.getPosX() - tile.getX(), (float) player.getPosYEye() - tile.getY(), (float) player.getPosZ() - tile.getZ());
    Vector3f PS = from.copy();
    PS.sub(P);
    Vector3f SE = to.copy();
    SE.sub(from);
    Vector3f adjustedVec = PS.copy();
    adjustedVec.cross(SE);
    adjustedVec.normalize();
    return adjustedVec;
  }

  public static void draw(TileLaser tile, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) throws Exception {
    BlockPos posTarget = tile.getPosTarget();
    if (posTarget == null || posTarget.equals(BlockPos.ZERO)) {
      return;
    }
    //now render
    matrixStackIn.push();
    Matrix4f positionMatrix = matrixStackIn.getLast().getMatrix();
    //diff between target and tile, targets always centered
    BlockPos tilePos = tile.getPos();
    Vector3f from = new Vector3f(
        posTarget.getX() + .5F - tilePos.getX(),
        posTarget.getY() + .5F - tilePos.getY(),
        posTarget.getZ() + .5F - tilePos.getZ());
    Vector3f to = new Vector3f(tile.xOffset.getOffset(), tile.yOffset.getOffset(), tile.zOffset.getOffset());
    IVertexBuilder builder = bufferIn.getBuffer(FakeBlockRenderTypes.LASER_MAIN_BEAM);
    drawDirewolfLaser(builder, positionMatrix, from, to, tile.getRed(), tile.getGreen(), tile.getBlue(), tile.getAlpha(), tile.getThick(), tilePos);
    //TODO: boolean to toggle core with tiny thickness
    final float coreThick = 0.01F;
    drawDirewolfLaser(builder, positionMatrix, from, to, 1, 1, 1, tile.getAlpha(), coreThick, tilePos);
    matrixStackIn.pop();
  }

  public static void drawDirewolfLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, BlockPos tilePos) {
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
    builder.pos(positionMatrix, p1.getX(), p1.getY(), p1.getZ())
        .color(r, g, b, alpha)
        .tex(1, v)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p3.getX(), p3.getY(), p3.getZ())
        .color(r, g, b, alpha)
        .tex(1, v)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p4.getX(), p4.getY(), p4.getZ())
        .color(r, g, b, alpha)
        .tex(0, v)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p2.getX(), p2.getY(), p2.getZ())
        .color(r, g, b, alpha)
        .tex(0, v)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
  }

  @Override
  public boolean isGlobalRenderer(TileLaser te) {
    return true;
  }
}
