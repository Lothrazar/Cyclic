package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderStructure implements BlockEntityRenderer<TileStructure> {


  public RenderStructure(BlockEntityRendererProvider.Context d) {   }

  @Override
  public void render(TileStructure te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    if (1 == te.getField(TileStructure.Fields.RENDER.ordinal())) {
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.isEmpty()) {
        UtilRender.renderOutline(te.getBlockPos(), te.getShape(), matrixStack, 0.7F, ClientConfigCyclic.getColor(te));
      }
      else {
        UtilRender.renderAsBlock(te.getBlockPos(), te.getShape(), matrixStack, stack, 1, 1);
      }
    }
  }
}
