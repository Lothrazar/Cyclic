package com.lothrazar.cyclic.block.tank;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclic.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

@OnlyIn(Dist.CLIENT)
public class RenderTank extends TileEntityRenderer<TileTank> {

  public RenderTank(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void func_225616_a_(TileTank tankHere, float v, MatrixStack matrixStack,
      IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluidStack = handler.getFluidInTank(0);
    if (fluidStack.isEmpty()) {
      return;
    }
    IFluidState fluidState = fluidStack.getFluid().getDefaultState();
    ResourceLocation reg = fluidState.getBlockState().getBlock().getRegistryName();
    double posY = 0.01 + (.985 * (fluidStack.getAmount() / tankHere.getCapacity()));
    //    System.out.println(reg.toString());
    // ok
    //render the fluid now
    //    BlockRendererDispatcher render = Minecraft.getInstance().getBlockRendererDispatcher();
    //  render.func_228794_a_(te.getPos(), light, buffer, fluidstate);
    ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
    BlockPos pos = tankHere.getPos();
    //  render.renderModel(p_228793_1_, p_228793_2_, p_228793_3_, p_228793_4_, p_228793_5_, p_228793_6_, p_228793_7_, modelData)
    /////https://www.minecraftforge.net/forum/topic/79556-1151-rendering-block-manually-clientside/?tab=comments#comment-379808
    RenderSystem.pushMatrix();
    matrixStack.func_227860_a_(); // push
    matrixStack.func_227861_a_(-renderInfo.getProjectedView().getX(), -renderInfo.getProjectedView().getY(), -renderInfo.getProjectedView().getZ()); // translate back to camera
    Matrix4f matrix4f = matrixStack.func_227866_c_().func_227870_a_(); // get final transformation matrix, handy to get yaw+pitch transformation
    RenderSystem.multMatrix(matrix4f);
    Minecraft.getInstance().getTextureManager().bindTexture(reg);
    Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    RenderUtil.drawBlock(Tessellator.getInstance().getBuffer(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, 0, 1, 0, 1, 0.9, posY, 0.9);
    Tessellator.getInstance().draw();
    matrixStack.func_227861_a_(0, 0, 0); // reset translation
    matrixStack.func_227865_b_(); // pop
    RenderSystem.popMatrix();
  }
}
