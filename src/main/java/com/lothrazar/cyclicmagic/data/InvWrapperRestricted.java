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
package com.lothrazar.cyclicmagic.data;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InvWrapperRestricted extends InvWrapper {

  private List<Integer> slotsAllowedInsert;
  private List<Integer> slotsAllowedExtract;

  public InvWrapperRestricted(ISidedInventory inv) {
    super(inv);
    slotsAllowedInsert = new ArrayList<Integer>();
    slotsAllowedExtract = new ArrayList<Integer>();
  }

  public List<Integer> getSlotsExtract() {
    return slotsAllowedExtract;
  }

  public void setSlotsExtract(List<Integer> slotsExport) {
    this.slotsAllowedExtract = slotsExport;
  }

  public List<Integer> getSlotsInsert() {
    return slotsAllowedInsert;
  }

  public void setSlotsInsert(List<Integer> slotsImport) {
    this.slotsAllowedInsert = slotsImport;
  }

  public boolean canInsert(int slot) {
    return this.getSlotsInsert().contains(slot);
  }

  public boolean canExtract(int slot) {
    return this.getSlotsExtract().contains(slot);
  }

  @Override
  @Nonnull
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (canInsert(slot) == false) {
      return stack;
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  @Nonnull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (canExtract(slot) == false) {
      return ItemStack.EMPTY;
    }
    return super.extractItem(slot, amount, simulate);
  }
}
