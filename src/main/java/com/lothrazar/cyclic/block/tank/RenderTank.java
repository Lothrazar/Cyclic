package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.render.type.FluidTankRenderType;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RenderTank implements BlockEntityRenderer<TileTank> {

  public RenderTank(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileTank tankHere, float v, PoseStack matrix,
      MultiBufferSource renderer, int light, int overlayLight) {
    IFluidHandler handler = tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    VertexConsumer buffer = renderer.getBuffer(FluidTankRenderType.RESIZABLE);
    matrix.scale(1F, FluidHelpers.getScale(tankHere.tank), 1F);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrix, buffer, RenderBlockUtils.getColorARGB(fluid, 0.1F),
        RenderBlockUtils.calculateGlowLight(light, fluid));
  }
}
