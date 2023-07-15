package com.lothrazar.cyclic.block.shapedata;

import java.awt.Color;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class RenderShapedata implements BlockEntityRenderer<TileShapedata> {

  public RenderShapedata(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileShapedata te, float v, PoseStack matrixStack,
      MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
    if (inv == null) {
      return;
    }
    if (1 == te.getField(TileShapedata.Fields.RENDER.ordinal())) {
      if (te.getTarget(0) != null) {
        RenderBlockUtils.renderOutline(te.getBlockPos(), te.getTarget(0), matrixStack, 1.05F, Color.BLUE);
      }
      if (te.getTarget(1) != null) {
        RenderBlockUtils.renderOutline(te.getBlockPos(), te.getTarget(1), matrixStack, 1.05F, Color.RED);
      }
    }
  }
}
