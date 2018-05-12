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
package com.lothrazar.cyclicmagic.block;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStore extends EnergyStorage {

  public static final int DEFAULT_CAPACITY = 1000 * 64;
  public static final int MAX_TRANSFER = 6400;

  public EnergyStore(int cap) {
    this(cap, true);
  }

  public EnergyStore(int cap, boolean canImportPower) {
    super(cap);
    this.maxReceive = (canImportPower) ? MAX_TRANSFER : 0;
    this.maxExtract = MAX_TRANSFER;
  }

  public void setEnergyStored(int en) {
    if (en < 0) {
      en = 0;
    }
    this.energy = Math.min(en, this.capacity);
  }

  public int emptyCapacity() {
    return this.capacity - this.energy;
  }
}
