package com.lothrazar.cyclic.block.laser;

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
 * laser rendering from this MIT project https://github.com/Direwolf20-MC/DireGoo2/blob/master/LICENSE.md
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderLaser extends TileEntityRenderer<TileLaser> {

  public RenderLaser(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileLaser te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    //    if (te.getField(TileHarvester.Fields.RENDER.ordinal()) == 1) {
    //      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack);
    //    }
    //    ModCyclic.LOGGER.info("VALID te.laserTimer  " + te.laserTimer);
    if (te.laserTimer >= 0) {
      //        UtilRender.renderLaser(new LaserConfig(te.laserTarget, te.getPos(), rotationTime, alpha, beamWidth, laserColor), matrixStack);
      // fallback 
      drawAllMiningLasers(te, matrixStack, partialTicks, iRenderTypeBuffer);
      //        UtilParticle.spawnParticle(te.getWorld(), ParticleTypes.PORTAL, te.laserTarget.down(), count);
    }
  }

  public static void drawAllMiningLasers(TileLaser tile, MatrixStack matrixStackIn, float f, IRenderTypeBuffer bufferIn) {
    //    BlockPos targetBlock = tile.getPos();//tile.laserTarget;//tile.getPos().up(3).east(5);//HACK TEST  //tile.getCurrentPos();
    //    if (targetBlock == BlockPos.ZERO || targetBlock == null) {
    //      //      return;
    //      targetBlock = tile.getPos().up(1).east(22);//HACK TEST 
    //    }
    matrixStackIn.push();
    Matrix4f positionMatrix2 = matrixStackIn.getLast().getMatrix();
    //    long gameTime = ;
    float diffX = 0.5F;//targetBlock.getX() + .5f - tile.getPos().getX();
    float diffY = 0.5F;//targetBlock.getY() + .5f - tile.getPos().getY();
    float diffZ = 0.5F;//targetBlock.getZ() + .5f - tile.getPos().getZ();  
    //    Direction facing = tile.getCurrentFacing();
    Vector3f to = new Vector3f(0.5F, 0.5F, 0.5F);
    //this is the REAL targetblock, above is fake news
    BlockPos pos = tile.laserTarget;
    BlockPos tilePos = tile.getPos();
    if (pos == null || pos.equals(BlockPos.ZERO)) {
      pos = tilePos.up(6);//.west(5);//HACK TEST 
      // return;
    }
    else {
      //      ModCyclic.LOGGER.info("VALID LASER " + pos);
    }
    diffX = pos.getX() + .5F - tilePos.getX();
    diffY = pos.getY() + .5F - tilePos.getY();
    diffZ = pos.getZ() + .5F - tilePos.getZ();
    Vector3f from = new Vector3f(diffX, diffY, diffZ);
    double v = tile.getWorld().getGameTime() * 0.04;
    IVertexBuilder builder = bufferIn.getBuffer(FakeBlockRenderTypes.LASER_MAIN_BEAM);
    drawMiningLaser(builder, positionMatrix2, from, to, tile.getRed(), tile.getGreen(), tile.getBlue(), tile.getAlpha(), tile.getThick(), v, v + diffY * 1.1, tile);
    //        builder = bufferIn.getBuffer(FakeBlockRenderTypes.LASER_MAIN_CORE);
    //        drawMiningLaser(builder, positionMatrix2, from, startLaser, 1, 1, 1, 1f, 0.02f, v, v + diffY - 2.5 * 1.5, tile);
    matrixStackIn.pop();
  }

  public static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, BlockPos tile) {
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

  public static void drawMiningLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, TileLaser tile) {
    Vector3f adjustedVec = adjustBeamToEyes(from, to, tile.getPos());
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
        .tex(1, (float) v1)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p3.getX(), p3.getY(), p3.getZ())
        .color(r, g, b, alpha)
        .tex(1, (float) v2)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p4.getX(), p4.getY(), p4.getZ())
        .color(r, g, b, alpha)
        .tex(0, (float) v2)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
    builder.pos(positionMatrix, p2.getX(), p2.getY(), p2.getZ())
        .color(r, g, b, alpha)
        .tex(0, (float) v1)
        .overlay(OverlayTexture.NO_OVERLAY)
        .lightmap(15728880)
        .endVertex();
  }

  @Override
  public boolean isGlobalRenderer(TileLaser te) {
    return true;
  }
}
