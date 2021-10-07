package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderShapedata implements BlockEntityRenderer<TileShapedata> {

  public RenderShapedata(BlockEntityRendererProvider.Context d) {   }

  @Override
  public void render(TileShapedata te, float v, PoseStack matrixStack,
      MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    if (1 == te.getField(TileShapedata.Fields.RENDER.ordinal())) {
      if (te.getTarget(0) != null) {
        UtilRender.renderOutline(te.getBlockPos(), te.getTarget(0), matrixStack, 1.05F, Color.BLUE);
      }
      if (te.getTarget(1) != null) {
        UtilRender.renderOutline(te.getBlockPos(), te.getTarget(1), matrixStack, 1.05F, Color.RED);
      }

    }
  }
}
