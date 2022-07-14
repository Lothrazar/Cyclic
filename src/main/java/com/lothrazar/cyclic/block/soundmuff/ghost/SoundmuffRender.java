package com.lothrazar.cyclic.block.soundmuff.ghost;

import com.lothrazar.cyclic.render.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SoundmuffRender implements BlockEntityRenderer<SoundmuffTile> {

  public SoundmuffRender(BlockEntityRendererProvider.Context d) {}

  @Override
  public boolean shouldRenderOffScreen(SoundmuffTile te) {
    return true;
  }

  @Override
  public void render(SoundmuffTile te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    ItemStack stack = inv.getStackInSlot(0);
    if (!stack.isEmpty()) {
      RenderUtils.renderAsBlock(te.getBlockPos(), te.getShape(), matrixStack, stack, 1F, 1F);
    }
  }
}
