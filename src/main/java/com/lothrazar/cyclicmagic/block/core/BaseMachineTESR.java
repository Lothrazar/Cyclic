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

import com.lothrazar.cyclicmagic.data.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class BaseMachineTESR<T extends TileEntityBaseMachineInvo> extends BaseTESR<T> {

  float red = 0.7F;
  float green = 0F;
  float blue = 1F;
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
  public void renderBasic(TileEntityBaseMachineInvo te) {
    //    ModCyclic.logger.error("Override renderBasic() in TESR");
  }

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
    if (te instanceof ITilePreviewToggle) {
      ITilePreviewToggle tilePreview = (ITilePreviewToggle) te;
      if (tilePreview.isPreviewVisible()) {
        UtilWorld.RenderShadow.renderBlockList(tilePreview.getShape(), te.getPos(), x, y, z, red, green, blue);
      }
    }
  }
}
