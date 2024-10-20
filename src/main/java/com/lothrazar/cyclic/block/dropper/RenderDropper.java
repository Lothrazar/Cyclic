package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class RenderDropper extends TileEntityRenderer<TileDropper> {

  public RenderDropper(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileDropper te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    int previewType = te.getField(TileDropper.Fields.RENDER.ordinal());
    if (PreviewOutlineType.SHADOW.ordinal() == previewType) {
      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.5F, ClientConfigCyclic.getColor(te));
    }
    else if (PreviewOutlineType.WIREFRAME.ordinal() == previewType) {
      for (BlockPos crd : te.getShapeHollow()) {
        UtilRender.createBox(matrixStack, crd, Vector3d.copy(te.getPos()));
      }
    }
  }
}
