package com.lothrazar.cyclic.block.sprinkler;

import com.lothrazar.cyclic.render.SpinModelRenderer;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class RenderSprinkler implements BlockEntityRenderer<TileSprinkler> {

  public RenderSprinkler(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileSprinkler tankHere, float v, PoseStack matrix,
      MultiBufferSource renderer, int light, int overlayLight) {
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(),tankHere.getBlockPos());//tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    FluidStack fluid = handler == null ? FluidStack.EMPTY : handler.getFluidInTank(0);
    boolean spinning = fluid != null && !fluid.isEmpty();
    float angle = 0F;
    if (spinning && tankHere.getLevel() != null) {
      angle = ((tankHere.getLevel().getGameTime() % 60L) + v) * 6F;
    }
    SpinModelRenderer.render(SpinModelRenderer.SPRINKLER_SPIN, angle, matrix, renderer, light, overlayLight);
    if (!spinning) {
      return;
    }
    VertexConsumer buffer = renderer.getBuffer(FluidTankRenderType.RESIZABLE);
    if (buffer.getClass().getName().equals("com.mojang.blaze3d.vertex.VertexMultiConsumer$Double")) {
      buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(FluidTankRenderType.RESIZABLE);
    }
    matrix.scale(1F, FluidHelpers.getScale(tankHere.tank) / 4F, 1F);
    float f = 0.5F;
    matrix.scale(f, 1F, f);
    matrix.translate(f, 0, f);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrix, buffer, RenderBlockUtils.getColorARGB(fluid),
        RenderBlockUtils.calculateGlowLight(light, fluid));
  }
}
