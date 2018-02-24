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
package com.lothrazar.cyclicmagic.component.library;
import net.minecraft.util.EnumFacing;

public enum QuadrantEnum {
  TL, TR, BL, BR;
  private static final float HALF = 0.5F;
  /**
   * using (x,y) in [0,1] determine quadrant of block hit
   * 
   * @param hitHoriz
   * @param hitVertic
   * @return
   */
  public static QuadrantEnum getFor(float hitHoriz, float hitVertic) {
    if (hitHoriz > HALF && hitVertic > HALF) {
      return TL;
    }
    else if (hitHoriz <= HALF && hitVertic > HALF) {
      return TR;
    }
    else if (hitHoriz > HALF && hitVertic < HALF) {
      return BL;
    }
    else {
      return BR;
    }
  }
  /**
   * based on facing side, convert either hitX or hitZ to hitHorizontal relative to player orientation
   * 
   * @param side
   * @param hitX
   * @param hitY
   * @param hitZ
   * @return
   */
  public static QuadrantEnum getForFace(EnumFacing side, float hitX, float hitY, float hitZ) {
    QuadrantEnum segment = null;
    switch (side) {
      case EAST:
        segment = QuadrantEnum.getFor(hitZ, hitY);
      break;
      case NORTH:
        segment = QuadrantEnum.getFor(hitX, hitY);
      break;
      case SOUTH:
        segment = QuadrantEnum.getFor(1 - hitX, hitY);
      break;
      case WEST:
        segment = QuadrantEnum.getFor(1 - hitZ, hitY);
      break;
      case UP:
      case DOWN:
      break;
    }
    return segment;
  }
}
