package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.render.type.FluidTankRenderType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;


public class RenderMelter implements BlockEntityRenderer<TileMelter> {

  public RenderMelter(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileMelter tankHere, float v, PoseStack matrixStack,
      MultiBufferSource buffer, int light, int overlayLight) {
    IItemHandler itemHandler = CapabilityUtil.item(tankHere.getLevel(),tankHere.getBlockPos());//tankHere.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).orElse(null);
    var level = tankHere.getLevel();
    if (itemHandler != null) {
      ItemStack stack = itemHandler.getStackInSlot(0);
      if (!stack.isEmpty()) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.60, 0.5);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, 0x111111, 200, matrixStack, buffer, level, light);
        matrixStack.popPose();
      }
      stack = itemHandler.getStackInSlot(1);
      if (!stack.isEmpty()) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.10, 0.5);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, 0x999999, 0, matrixStack, buffer, level, light);
        matrixStack.popPose();
      }
    }
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(),tankHere.getBlockPos());//tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    VertexConsumer vertexBuffer = buffer.getBuffer(FluidTankRenderType.RESIZABLE);
    //skip during block-breaking overlay (vertex format mismatch with SheetedDecalTextureGenerator)
    if (vertexBuffer.getClass().getName().equals("com.mojang.blaze3d.vertex.VertexMultiConsumer$Double")) {
      return;
    }
    matrixStack.pushPose();
    matrixStack.scale(1F, FluidHelpers.getScale(tankHere.tank), 1F);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrixStack, vertexBuffer, RenderBlockUtils.getColorARGB(fluid),
        RenderBlockUtils.calculateGlowLight(light, fluid));
    matrixStack.popPose();
  }
}
