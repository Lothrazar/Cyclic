package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class RenderFluidCollect extends TileEntityRenderer<TileFluidCollect> {

  public RenderFluidCollect(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileFluidCollect te, float v, MatrixStack matrix, IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileFluidCollect.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      UtilRender.renderOutline(te.getPos(), te.getShapeHollow(), matrix, 0.4F, ClientConfigCyclic.getColor(te));
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShapeHollow()) {
        UtilRender.createBox(matrix, crd, Vector3d.copy(te.getPos()));
      }
    }
  }
}
