package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.render.RenderUtils;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RenderSprinkler implements BlockEntityRenderer<TileSprinkler> {

  public RenderSprinkler(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileSprinkler tankHere, float v, PoseStack matrix,
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
    matrix.scale(1F, FluidHelpers.getScale(tankHere.tank) / 4F, 1F);
    float f = 0.5F;
    matrix.scale(f, 1F, f);
    matrix.translate(f, 0, f);
    RenderUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrix, buffer, RenderUtils.getColorARGB(fluid, 0.1F),
        RenderUtils.calculateGlowLight(light, fluid));
  }
}
