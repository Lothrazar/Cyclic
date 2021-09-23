package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderHarvester extends TileEntityRenderer<TileHarvester> {

  public RenderHarvester(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileHarvester te, float v, MatrixStack matrix,
      IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    if (1 == te.getField(TileHarvester.Fields.RENDER.ordinal())) {
      UtilRender.renderOutline(te.getPos(), te.getShapeHollow(), matrix, 0.7F, ClientConfigCyclic.getColor(te));
    }
  }
}
