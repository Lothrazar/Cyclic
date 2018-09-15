/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.sprinkler;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.block.core.BaseTESR;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

public class SprinklerTESR<T extends TileSprinkler> extends BaseTESR<T> {

  public SprinklerTESR(Block block) {
    super(block);
  }

  @Override
  public void render(TileSprinkler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    this.renderAnimation(te);
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }

  protected void renderAnimation(TileEntityBaseMachineInvo te) {
    GlStateManager.pushMatrix();
    if (te.isRunning()) {
      //start of rotate
      GlStateManager.translate(0.5, 0, 0.5);
      long angle = (System.currentTimeMillis() / 10) % 360;
      GlStateManager.rotate(angle, 0, 1, 0);
      GlStateManager.translate(-.5, 0, -.5);
      //end of rotate
    }
    RenderHelper.disableStandardItemLighting();
    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    if (Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }
    else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }
    World world = te.getWorld();
    // Translate back to local view coordinates so that we can do the acual rendering here
    GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
    Tessellator tessellator = Tessellator.getInstance();
    tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
        world,
        getBakedModel(),
        world.getBlockState(te.getPos()),
        te.getPos(),
        Tessellator.getInstance().getBuffer(), false);
    tessellator.draw();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popMatrix();
  }
}
