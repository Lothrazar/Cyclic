package com.lothrazar.cyclicmagic.component.bucketstorage;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;
import com.google.common.base.Function;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;

public class FluidTESR extends TileEntitySpecialRenderer<TileEntityBucketStorage> {
  @Override
  public void render(TileEntityBucketStorage te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    Minecraft mc = Minecraft.getMinecraft();
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
      float[] color = new float[] { red, green, blue, 1.0F };//cheat the alpha
      // THANKS FOR POST http://www.minecraftforge.net/forum/topic/44388-1102-render-fluid-level-in-tank-with-tesr/
      //TOP SIDE
      buffer.setTranslation(x, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(2F / 16F, posY, 2F / 16F).tex(still.getInterpolatedU(4), still.getInterpolatedV(4)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(12F / 16F, posY, 2F / 16F).tex(still.getInterpolatedU(12), still.getInterpolatedV(4)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(12F / 16F, posY, 12F / 16F).tex(still.getInterpolatedU(12), still.getInterpolatedV(12)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(2F / 16F, posY, 12F / 16F).tex(still.getInterpolatedU(4), still.getInterpolatedV(12)).color(color[0], color[1], color[2], color[3]).endVertex();
      tess.draw();
      //BOTTOM SIDE
      buffer.setTranslation(x, y-posY, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(4F / 16F, posY, 4F / 16F).tex(still.getInterpolatedU(4), still.getInterpolatedV(4)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(12F / 16F, posY, 4F / 16F).tex(still.getInterpolatedU(12), still.getInterpolatedV(4)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(12F / 16F, posY, 12F / 16F).tex(still.getInterpolatedU(12), still.getInterpolatedV(12)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(4F / 16F, posY, 12F / 16F).tex(still.getInterpolatedU(4), still.getInterpolatedV(12)).color(color[0], color[1], color[2], color[3]).endVertex();
      tess.draw();
      //the +Z side
      buffer.setTranslation(x, y, z);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
      buffer.pos(14F / 16F, 1F / 16F, 14F / 16F).tex(flow.getInterpolatedU(12), flow.getInterpolatedV(15)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(14F / 16F, posY, 14F / 16F).tex(flow.getInterpolatedU(12), flow.getInterpolatedV(1)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(2F / 16F, posY, 14F / 16F).tex(flow.getInterpolatedU(4), flow.getInterpolatedV(1)).color(color[0], color[1], color[2], color[3]).endVertex();
      buffer.pos(2F / 16F, 1F / 16F, 14F / 16F).tex(flow.getInterpolatedU(4), flow.getInterpolatedV(15)).color(color[0], color[1], color[2], color[3]).endVertex();
      tess.draw();
//now the opposite: -Z side
//      buffer.setTranslation(x, y, z+1);
//      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//      buffer.pos(12F / 16F, 1F / 16F,- 12F / 16F).tex(flow.getInterpolatedU(12), flow.getInterpolatedV(15)).color(color[0], color[1], color[2], color[3]).endVertex();
//      buffer.pos(12F / 16F, posY, -12F / 16F).tex(flow.getInterpolatedU(12), flow.getInterpolatedV(1)).color(color[0], color[1], color[2], color[3]).endVertex();
//      buffer.pos(4F / 16F, posY, -12F / 16F).tex(flow.getInterpolatedU(4), flow.getInterpolatedV(1)).color(color[0], color[1], color[2], color[3]).endVertex();
//      buffer.pos(4F / 16F, 1F / 16F, -12F / 16F).tex(flow.getInterpolatedU(4), flow.getInterpolatedV(15)).color(color[0], color[1], color[2], color[3]).endVertex();
//      tess.draw();
      buffer.setTranslation(0, 0, 0);
    }
    GlStateManager.popMatrix();
  }
}
