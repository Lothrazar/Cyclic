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
package com.lothrazar.cyclicmagic.block.imbue;

import com.lothrazar.cyclicmagic.core.block.BaseMachineTESR;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ImbueTESR extends BaseMachineTESR<TileEntityImbue> {

  public ImbueTESR() {
    super();
  }

  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    //four corners if they exist
    renderItem(te, te.getStackInSlot(1), 0, 0.5F, 1);
    // renderItem(te, te.getStackInSlot(2), 1, 0.5F, 1);
    renderItem(te, te.getStackInSlot(2), 1, 0.5F, 0);
    // renderItem(te, te.getStackInSlot(4), 0, 0.5F, 0);
    //and the enchanted item goes here
    renderItem(te, te.getStackInSlot(0), 0.5F, 0.85F, 0.5F);
  }
}
