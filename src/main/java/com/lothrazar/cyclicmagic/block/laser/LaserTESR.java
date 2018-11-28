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
package com.lothrazar.cyclicmagic.block.laser;

import com.lothrazar.cyclicmagic.block.core.BaseTESR;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.util.RenderUtil;
import com.lothrazar.cyclicmagic.util.RenderUtil.LaserConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LaserTESR extends BaseTESR<TileEntityLaser> {

  public LaserTESR(Block b) {
    super(b);
  }

  @Override
  public void render(TileEntityLaser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    //   super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    //find laser endpoints and go
    if (te.isRunning() == false) {
      return;
    }
    float[] color = te.getColor();//new float[] { te.getField(Fields), 0F, 0.5F };
    double rotationTime = te.isPulsing() ? 120 : 0;
    double beamWidth = 0.09;
    BlockPos first = te.getPos();
    BlockPosDim second = null;
    int timer = TileEntityLaser.MAX_TIMER;
    if (te.isExtending()) {
      timer = te.getField(TileEntityLaser.Fields.TIMER.ordinal());
    }
    for (int i = 0; i < te.getSizeInventory(); i++) {
      second = te.getTarget(i);
      if (second != null && second.toBlockPos() != null && second.dimension == te.getDimension()) {
        LaserConfig laserCnf = new LaserConfig(first, second.toBlockPos(),
            rotationTime, te.alphaCalculated(), beamWidth, color);
        laserCnf.xOffset = te.getxOffset();
        laserCnf.yOffset = te.getyOffset();
        laserCnf.zOffset = te.getzOffset();
        //     
        laserCnf.timer = timer;
        RenderUtil.renderLaser(laserCnf);
      }
    }
  }

  @Override
  public boolean isGlobalRenderer(TileEntityLaser te) {
    return true;
  }
}
