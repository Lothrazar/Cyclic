package com.lothrazar.cyclic.block.forester;

import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderForester extends TileEntityRenderer<TileForester> {

  public RenderForester(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileForester te, float v, MatrixStack matrixStack,
      IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    // ok
    if (te.getField(TileForester.Fields.RENDER.ordinal()) == 1)
      UtilRender.renderOutline(te.getPos(), te.getShapeHollow(), matrixStack);
  }
}
