package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderDropper extends TileEntityRenderer<TileDropper> {

  public RenderDropper(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileDropper te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDropper.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.5F, Color.MAGENTA);
    }
  }
}
