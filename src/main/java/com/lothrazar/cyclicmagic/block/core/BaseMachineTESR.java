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
package com.lothrazar.cyclicmagic.block.core;

import javax.annotation.Nonnull;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public abstract class BaseMachineTESR<T extends TileEntityBaseMachineInvo> extends BaseTESR<T> {

  protected int itemSlotAbove = -1;

  public BaseMachineTESR(Block res, int slot) {
    super(res);
    this.itemSlotAbove = slot;
  }

  public BaseMachineTESR(int slot) {
    this(null, slot);
  }

  public BaseMachineTESR() {
    this(null, -1);
  }

  /**
   * override this in your main class to call other animation hooks
   * 
   * @param te
   */
  public abstract void renderBasic(TileEntityBaseMachineInvo te);

  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z,
      float partialTicks, int destroyStage, float alpha
  //, net.minecraft.client.renderer.BufferBuilder buffer
  ) {
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    if (te.isRunning()) {// && te.hasEnoughFuel()
      this.renderBasic(te);
    }
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }

  protected void renderAnimation(@Nonnull TileEntityBaseMachineInvo te) {
    if (Minecraft.getMinecraft() == null
        || Minecraft.getMinecraft().getBlockRendererDispatcher() == null
        || Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer() == null
        || getBakedModel() == null) {
      ModCyclic.logger.error("TESR render animation caught by null check");
      return;
    }
    GlStateManager.pushMatrix();
    EnumFacing facing = te.getCurrentFacing();
    if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
      GlStateManager.rotate(90, 0, 1, 0);
      GlStateManager.translate(-1, 0, 0);//fix position and such
    }
    ////do the sliding across animation
    double currTenthOfSec = System.currentTimeMillis() / 100;//move speed
    double ratio = (currTenthOfSec % 8) / 10.00;//this is dong modulo 0.8 since there are 8 locations to move over
    GlStateManager.translate(0, 0, -1 * ratio);
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
    try {
      GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
      Tessellator tessellator = Tessellator.getInstance();
      //if buffer had an "isDrawing" here, i would halt if that is true
      tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
      //crash on line below, NPE, not sure why. very rare I guess?
      Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
          world,
          getBakedModel(),
          world.getBlockState(te.getPos()),
          te.getPos(),
          Tessellator.getInstance().getBuffer(), false);
      tessellator.draw();
    }
    catch (IllegalStateException alreadyBuilding) {
      ModCyclic.logger.info("Already building!  BufferBuilder:isDrawing == true I suppose: " + alreadyBuilding.getMessage());
      //BufferBuilder has a private flag "isDrawing", and if its true it throws this exceptoin
      //problem: there is no GET method or way to detect "is this drawing" before I start.
      //instead I catch and ignore this exception/
    }
    catch (Exception e) {
      ModCyclic.logger.error("TESR render baked model exception", e);
    }
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popMatrix();
  }
}
