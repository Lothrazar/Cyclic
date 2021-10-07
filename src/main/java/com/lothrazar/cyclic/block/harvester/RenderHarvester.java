package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class RenderHarvester extends BlockEntityRenderer<TileHarvester> {

  public RenderHarvester(BlockEntityRenderDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileHarvester te, float v, PoseStack matrix,
      MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    if (1 == te.getField(TileHarvester.Fields.RENDER.ordinal())) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShapeHollow(), matrix, 0.7F, ClientConfigCyclic.getColor(te));
    }
  }
}
