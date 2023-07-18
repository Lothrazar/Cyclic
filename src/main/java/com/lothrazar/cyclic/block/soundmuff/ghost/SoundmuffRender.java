package com.lothrazar.cyclic.block.soundmuff.ghost;

import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class SoundmuffRender implements BlockEntityRenderer<SoundmuffTile> {

  public SoundmuffRender(BlockEntityRendererProvider.Context d) {}

  @Override
  public boolean shouldRenderOffScreen(SoundmuffTile te) {
    return true;
  }

  @Override
  public void render(SoundmuffTile te, float v, PoseStack matrixStack, MultiBufferSource ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
    if (inv == null) {
      return;
    }
    ItemStack stack = inv.getStackInSlot(0);
    if (!stack.isEmpty()) {
      RenderBlockUtils.renderAsBlock(te.getLevel(), te.getBlockPos(), te.getShape(), matrixStack, stack, 1, 1);
    }
  }
}
