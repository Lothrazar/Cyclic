package com.lothrazar.cyclic.block.tank;

import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class RenderTank extends TileEntityRenderer<TileTank> {

  @Override
  public void renderTileEntityFast(TileTank tankHere, double x, double y, double z, float partialTicks, int destroyStage, net.minecraft.client.renderer.BufferBuilder bufferDoNotUse) {
    //test 
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluidStack = handler.getFluidInTank(0);
    if (fluidStack.isEmpty()) {
      return;
    }
    GlStateManager.pushMatrix();
    GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glDisable(GL11.GL_LIGHTING);
    //   
    //START
    Tessellator tessy = Tessellator.getInstance();
    AtlasTexture atlas = Minecraft.getInstance().getTextureMap();
    TextureAtlasSprite still = atlas.getSprite(fluidStack.getFluid().getAttributes().getStill(tankHere.getWorld(), tankHere.getPos()));
    TextureAtlasSprite flow = atlas.getSprite(fluidStack.getFluid().getAttributes().getFlowing(tankHere.getWorld(), tankHere.getPos()));
    //ok good
    double posY = 0.01 + (.985 * (fluidStack.getAmount() / tankHere.getCapacity()));
    int icolor = 255;//???? fluidStack.getFluid().getColor(fluidStack);
    //RGB encoded in hexval integer
    float red = (icolor >> 16 & 0xFF) / 255.0F;
    float green = (icolor >> 8 & 0xFF) / 255.0F;
    float blue = (icolor & 0xFF) / 255.0F;
    float alph = 1.0F;
    // THANKS FOR POST http://www.minecraftforge.net/forum/topic/44388-1102-render-fluid-level-in-tank-with-tesr/
    // T/B for top and bottom
    float T = 15.9F / 16F;
    float B = 0.1F / 16F;
    int single = 1;
    int S = 1, E = 15;//for start and end. vertex ranges from [0,16];
    //TOP SIDE
    BufferBuilder buffer = tessy.getBuffer();
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.setTranslation(x, y, z);
    buffer.pos(B, posY, single).tex(still.getInterpolatedU(S), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, single).tex(still.getInterpolatedU(E), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(single, posY, B).tex(still.getInterpolatedU(E), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, posY, B).tex(still.getInterpolatedU(S), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    //BOTTOM SIDE
    buffer.setTranslation(x, y - posY + B, z);//
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.pos(B, posY, B).tex(still.getInterpolatedU(S), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, B).tex(still.getInterpolatedU(E), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, T).tex(still.getInterpolatedU(E), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, posY, T).tex(still.getInterpolatedU(S), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    //DONE
    //the +Z side 
    buffer.setTranslation(x, y, z);
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.pos(T, B, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, posY, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, B, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    //now the opposite: -Z side 
    buffer.setTranslation(x, y, z + 1);
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.pos(T, posY, -1 * T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, B, -1 * T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, B, -1 * T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(B, posY, -1 * T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    // the +X side  
    buffer.setTranslation(x, y, z);
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.pos(T, B, B).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, B).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, B, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    // the -X side  
    buffer.setTranslation(x - 1 + 2 * B, y, z);
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    buffer.pos(T, posY, B).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, B, B).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, B, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
    buffer.pos(T, posY, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
    tessy.draw();
    buffer.setTranslation(0, 0, 0);
    GL11.glPopAttrib();
    GlStateManager.popMatrix();
  }
}
