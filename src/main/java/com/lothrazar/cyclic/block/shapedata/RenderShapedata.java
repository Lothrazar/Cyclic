package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3d;

public class RenderShapedata extends TileEntityRenderer<TileShapedata> {

  public RenderShapedata(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileShapedata te, float v, MatrixStack matrixStack, IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    //    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    //    if (inv == null) {
    //      return;
    //    }
    int previewType = te.getField(TileShapedata.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      if (te.getTarget(0) != null) {
        UtilRender.renderOutline(te.getPos(), te.getTarget(0), matrixStack, 1.05F, Color.BLUE);
      }
      if (te.getTarget(1) != null) {
        UtilRender.renderOutline(te.getPos(), te.getTarget(1), matrixStack, 1.05F, Color.RED);
      }
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      //      for (BlockPos crd : te.getShapeHollow()) {
      UtilRender.createBox(matrixStack, te.getTarget(0), Vector3d.copy(te.getPos()));
      UtilRender.createBox(matrixStack, te.getTarget(1), Vector3d.copy(te.getPos()));
      //      }
    }
  }
}
