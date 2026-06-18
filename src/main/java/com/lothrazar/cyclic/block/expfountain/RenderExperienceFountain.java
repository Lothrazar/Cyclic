package com.lothrazar.cyclic.block.expfountain;

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

public class RenderExperienceFountain implements BlockEntityRenderer<TileExperienceFountain> {

  public RenderExperienceFountain(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileExperienceFountain tankHere, float v, PoseStack matrix,
      MultiBufferSource renderer, int light, int overlayLight) {
    float angle = 0F;
    if (tankHere.getLevel() != null && tankHere.getLevel().hasNeighborSignal(tankHere.getBlockPos())) {
      angle = ((tankHere.getLevel().getGameTime() % 60L) + v) * 6F;
    }
    SpinModelRenderer.render(SpinModelRenderer.FOUNTAIN_SPIN, angle, matrix, renderer, light, overlayLight);
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(), tankHere.getBlockPos());
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
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
