package com.lothrazar.cyclic.block.soundmuff;

import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class SoundmuffRender extends TileEntityRenderer<SoundmuffTile> {

  public SoundmuffRender(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public boolean isGlobalRenderer(SoundmuffTile te) {
    return true;
  }

  @Override
  public void render(SoundmuffTile te, float v, MatrixStack matrixStack, IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    ItemStack stack = inv.getStackInSlot(0);
    if (!stack.isEmpty()) {
      UtilRender.renderAsBlock(te.getPos(), te.getShape(), matrixStack, stack, 1F, 1F);
    }
  }
}
