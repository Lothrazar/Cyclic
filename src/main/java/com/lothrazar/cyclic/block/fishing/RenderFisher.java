package com.lothrazar.cyclic.block.fishing;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderFisher extends TileEntityRenderer<TileFisher> {

  public RenderFisher(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileFisher tankHere, float v, MatrixStack matrixStack,
      IRenderTypeBuffer buffer, int light, int overlayLight) {
    IItemHandler itemHandler = tankHere.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
    if (itemHandler != null) {
      ItemStack stack = itemHandler.getStackInSlot(0);
      if (!stack.isEmpty()) {
        matrixStack.push();
        //        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(0.4, 0.45, 0.4);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.GROUND, 0x910911, 0, matrixStack, buffer);
        matrixStack.pop();
      }
    }
  }
}
