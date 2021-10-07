package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class RenderDetectorItem implements BlockEntityRenderer<TileDetectorItem> {

//  public RenderDetectorItem(BlockEntityRenderDispatcher d) {
//    super(d);
//  }

  @Override
  public void render(TileDetectorItem te, float v, PoseStack matrixStack,
      MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDetectorItem.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.6F, ClientConfigCyclic.getColor(te));
    }
  }
}
