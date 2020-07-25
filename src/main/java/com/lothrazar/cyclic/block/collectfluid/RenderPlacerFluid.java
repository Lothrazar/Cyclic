package com.lothrazar.cyclic.block.collectfluid;

import java.awt.Color;
import java.util.List;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPlacerFluid extends TileEntityRenderer<TileFluidCollect> {

  public RenderPlacerFluid(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileFluidCollect te, float v, MatrixStack matrix,
      IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    if (1 == te.getField(TileFluidCollect.Fields.RENDER.ordinal())) {
      renderOutline(te, matrix);
    }
  }

  private void renderOutline(TileFluidCollect te, MatrixStack matrix) {
    //    IRenderTypeBuffer.getImpl(ibuffer);
    final Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    //    World world = ModCyclic.proxy.getClientWorld();
    List<BlockPos> coords = te.getShape();
    matrix.push();
    BlockPos view = te.getPos();//mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    IVertexBuilder builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.SOLID_COLOUR);
    for (BlockPos e : coords) {
      //      if (!world.isAirBlock(e)) {
      //        continue;
      //      }
      matrix.push();
      matrix.translate(e.getX(), e.getY(), e.getZ());
      matrix.translate(-0.005f, -0.005f, -0.005f);
      float scale = 0.7F;
      matrix.scale(scale, scale, scale);
      matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
      Matrix4f positionMatrix = matrix.getLast().getMatrix();
      Color color = Color.BLUE;
      UtilRender.renderCube(positionMatrix, builder, e, color);
      matrix.pop();
    }
    matrix.pop();
    //    RenderSystem.disableDepthTest();
    buffer.finish(FakeBlockRenderTypes.SOLID_COLOUR);
  }
}
