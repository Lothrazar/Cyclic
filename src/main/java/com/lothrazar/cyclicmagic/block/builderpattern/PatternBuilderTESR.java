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
package com.lothrazar.cyclicmagic.block.builderpattern;

import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.builderpattern.TileEntityPatternBuilder.RenderType;
import com.lothrazar.cyclicmagic.block.core.BaseMachineTESR;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PatternBuilderTESR extends BaseMachineTESR<TileEntityPatternBuilder> {

  public PatternBuilderTESR() {
    super();
  }

  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {}

  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
    if (te instanceof TileEntityPatternBuilder == false) {
      return;
    }
    TileEntityPatternBuilder tile = ((TileEntityPatternBuilder) te);
    RenderType render = tile.getRenderType();
    if (render == RenderType.OFF) {
      return;
    }
    List<BlockPos> targetShape = tile.getTargetShape();
    if (render == RenderType.OUTLINE) {
      UtilWorld.RenderShadow.renderBlockPos(tile.getSourceCenter().up(tile.getHeight() / 2), te.getPos(), x, y, z, 0F, 0F, 0.5F);
      UtilWorld.RenderShadow.renderBlockPos(tile.getTargetCenter().up(tile.getHeight() / 2), te.getPos(), x, y, z, .5F, 0, 0);
      UtilWorld.RenderShadow.renderBlockList(tile.getSourceFrameOutline(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
      //then do target outline
      UtilWorld.RenderShadow.renderBlockList(tile.getTargetFrameOutline(), te.getPos(), x, y, z, 1F, 1F, 1F);
      UtilWorld.RenderShadow.renderBlockList(targetShape, te.getPos(), x, y, z, .1F, .1F, .1F);
    }
    else if (targetShape.size() > 0) {
      boolean isSolid = render == RenderType.SOLID;
      //do target shape
      //OLD WAY WAS
      // UtilWorld.RenderShadow.renderBlockList(targetShape, te.getPos(), x, y, z, .1F, .1F, .1F);
      try {
        Map<BlockPos, IBlockState> shapeModel = tile.getShapeFancy(tile.getSourceShape(), targetShape);
        for (Map.Entry<BlockPos, IBlockState> entry : shapeModel.entrySet()) {
          //
          UtilWorld.RenderShadow.renderBlockPhantom(te.getWorld(), te.getPos(), entry.getValue(), x, y, z,
              entry.getKey(), isSolid);
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("render blockModel phantom error", e);
      }
    }
  }
}
