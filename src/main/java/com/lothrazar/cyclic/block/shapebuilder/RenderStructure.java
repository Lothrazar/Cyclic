package com.lothrazar.cyclic.block.shapebuilder;

import java.awt.Color;
import java.util.List;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderStructure extends TileEntityRenderer<TileStructure> {

  public RenderStructure(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileStructure te, float v, MatrixStack matrix,
      IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    // ok 
    //    IRenderTypeBuffer.getImpl(ibuffer);
    final Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    //    World world = ModCyclic.proxy.getClientWorld();
    List<BlockPos> coords = te.getShape();
    Vec3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.push();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    IVertexBuilder builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.TRANSPARENT_SOLID_COLOUR);
    for (BlockPos e : coords) {
      matrix.push();
      matrix.translate(e.getX(), e.getY(), e.getZ());
      //      matrix.translate(-0.005f, -0.005f, -0.005f);
      float scale = 0.5F;
      matrix.scale(scale, scale, scale);
      matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
      Matrix4f positionMatrix = matrix.getLast().getMatrix();
      Color color = Color.BLUE;
      UtilRender.renderCube(positionMatrix, builder, e, color);
      matrix.pop();
    }
    matrix.pop();
    //    RenderSystem.disableDepthTest();
    buffer.finish(FakeBlockRenderTypes.TRANSPARENT_SOLID_COLOUR);
  }
}
