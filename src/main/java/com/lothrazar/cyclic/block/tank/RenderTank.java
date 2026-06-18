package com.lothrazar.cyclic.block.tank;

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

public class RenderTank implements BlockEntityRenderer<TileTank> {

  public RenderTank(BlockEntityRendererProvider.Context d) {}

  @Override
  public void render(TileTank tankHere, float v, PoseStack matrix,
      MultiBufferSource renderer, int light, int overlayLight) {
    IFluidHandler handler = CapabilityUtil.fluid(tankHere.getLevel(),tankHere.getBlockPos());//tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    //when the block is being broken, the passed MultiBufferSource wraps each consumer in a
    //SheetedDecalTextureGenerator that requires a Normal vertex element. Our render type uses
    //POSITION_COLOR_TEX_LIGHTMAP which has no Normal, so writing to the wrapped consumer crashes.
    //Bypass it by going directly to the main buffer source - the fluid stays visible during
    //breaking without the crack overlay (cracks are on the glass frame, not on the fluid inside).
    MultiBufferSource bufferSource = renderer;
    if (renderer.getBuffer(FluidTankRenderType.RESIZABLE).getClass().getName()
        .equals("com.mojang.blaze3d.vertex.VertexMultiConsumer$Double")) {
      bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
    }
    VertexConsumer buffer = bufferSource.getBuffer(FluidTankRenderType.RESIZABLE);
    matrix.scale(1F, FluidHelpers.getScale(tankHere.tank), 1F);
    RenderBlockUtils.renderObject(FluidHelpers.getFluidModel(fluid, FluidHelpers.STAGES - 1),
        matrix, buffer, RenderBlockUtils.getColorARGB(fluid),
        RenderBlockUtils.calculateGlowLight(light, fluid));
  }
}
