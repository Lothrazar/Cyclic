package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderMelter implements BlockEntityRenderer<TileMelter> {


  public RenderMelter(BlockEntityRendererProvider.Context d) {   }

  @Override
  public void render(TileMelter tankHere, float v, PoseStack matrixStack,
      MultiBufferSource buffer, int light, int overlayLight) {
    //TODO: recipe from tag
    IItemHandler itemHandler = tankHere.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
    if (itemHandler != null) {
      ItemStack stack = itemHandler.getStackInSlot(0);
      if (!stack.isEmpty()) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.60, 0.5);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.GROUND, 0x111111, 200, matrixStack, buffer,light);
        matrixStack.popPose();
      }
      stack = itemHandler.getStackInSlot(1);
      if (!stack.isEmpty()) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.10, 0.5);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.GROUND, 0x999999, 0, matrixStack, buffer,light);
        matrixStack.popPose();
      }
    }
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    VertexConsumer vertexBuffer = buffer.getBuffer(FluidTankRenderType.resizableCuboid());
    matrixStack.pushPose();
    matrixStack.scale(1F, UtilFluid.getScale(tankHere.tank), 1F);
    UtilRender.renderObject(UtilFluid.getFluidModel(fluid, UtilFluid.STAGES - 1),
        matrixStack, vertexBuffer, UtilRender.getColorARGB(fluid, 0.1F),
        UtilRender.calculateGlowLight(light, fluid));
    matrixStack.popPose();
  }
}
