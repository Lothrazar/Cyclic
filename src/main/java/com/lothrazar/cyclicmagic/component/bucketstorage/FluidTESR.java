package com.lothrazar.cyclicmagic.component.bucketstorage;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;

public class FluidTESR extends TileEntitySpecialRenderer<TileEntityBucketStorage> {
  @Override
  public void render(TileEntityBucketStorage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    
    FluidStack fluid = te.getCurrentFluid();
    if (fluid == null) { return; }
    ModCyclic.logger.info(fluid.getFluid().getStill(fluid).toString());
    //TextureAtlasSprite flowing = mc.getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
    GlStateManager.pushMatrix();
    if (fluid != null) {
      Tessellator tess = Tessellator.getInstance();
      BufferBuilder buffer = tess.getBuffer();
      bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());
      TextureAtlasSprite flow = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getFlowing().toString());
      double posY = 0.9;// .1 + (.8 * ((float) fluid.amount / (float) TileEntityBucketStorage.TANK_FULL));
      int icolor = fluid.getFluid().getColor(fluid);
      float red = (icolor >> 16 & 0xFF) / 255.0F;
      float green = (icolor >> 8 & 0xFF) / 255.0F;
      float blue = (icolor & 0xFF) / 255.0F;
      float alph = 1.0F;
      // THANKS FOR POST http://www.minecraftforge.net/forum/topic/44388-1102-render-fluid-level-in-tank-with-tesr/
      // T/B for top and bottom
      float T = 15F / 16F;
      float B = 1F / 16F;
      int S = 1, E = 15;//for start and end. vertex ranges from [0,16];
      //TOP SIDE
      buffer.setTranslation(x, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(B, posY, T).tex(still.getInterpolatedU(S), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, T).tex(still.getInterpolatedU(E), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, B).tex(still.getInterpolatedU(E), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, posY, B).tex(still.getInterpolatedU(S), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      tess.draw();
      //BOTTOM SIDE
      buffer.setTranslation(x, y - posY + B, z);//
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(B, posY, B).tex(still.getInterpolatedU(S), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, B).tex(still.getInterpolatedU(E), still.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, T).tex(still.getInterpolatedU(E), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, posY, T).tex(still.getInterpolatedU(S), still.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      tess.draw();
      //the +Z side 
      buffer.setTranslation(x, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(T, B, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, posY, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, B, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      tess.draw();
      //now the opposite: -Z side 
      buffer.setTranslation(x, y, z + 1);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(T, posY, -1 * T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, B, -1 * T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, B, -1 * T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(B, posY, -1 * T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      tess.draw();
      // the +X side  
      buffer.setTranslation(x, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(T, B, B).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, B).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, B, T).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      tess.draw();
      // the -X side  
      buffer.setTranslation(x - 1 + 2 * B, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(T, posY, B).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, B, B).tex(flow.getInterpolatedU(S), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, B, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(E)).color(red, green, blue, alph).endVertex();
      buffer.pos(T, posY, T).tex(flow.getInterpolatedU(E), flow.getInterpolatedV(S)).color(red, green, blue, alph).endVertex();
      tess.draw();
      buffer.setTranslation(0, 0, 0);
    }
    GlStateManager.popMatrix();
  }
}
