package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RenderSprinkler extends BlockEntityRenderer<TileSprinkler> {

  public RenderSprinkler(BlockEntityRenderDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileSprinkler tankHere, float v, PoseStack matrix,
      MultiBufferSource renderer, int light, int overlayLight) {
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    VertexConsumer buffer = renderer.getBuffer(FluidTankRenderType.resizableCuboid());
    matrix.scale(1F, UtilFluid.getScale(tankHere.tank) / 4F, 1F);
    float f = 0.5F;
    matrix.scale(f, 1F, f);
    matrix.translate(f, 0, f);
    UtilRender.renderObject(UtilFluid.getFluidModel(fluid, UtilFluid.STAGES - 1),
        matrix, buffer, UtilRender.getColorARGB(fluid, 0.1F),
        UtilRender.calculateGlowLight(light, fluid));
  }
}
