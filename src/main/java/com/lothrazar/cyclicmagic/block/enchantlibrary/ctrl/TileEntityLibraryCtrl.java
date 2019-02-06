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
package com.lothrazar.cyclicmagic.block.enchantlibrary.ctrl;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.block.enchantlibrary.EnchantStorageTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityLibraryCtrl extends TileEntityBaseMachineInvo implements ITickable {

  private static final int SLOT_IN = 0;
  private static final int SLOT_OUT = 1;

  public TileEntityLibraryCtrl() {
    super(2);
    this.setSlotsForInsert(SLOT_IN);
    this.setSlotsForExtract(SLOT_OUT);
  }

  @Override
  public void update() {
    ItemStack stack = this.getStackInSlot(SLOT_IN);
    if (stack.isEmpty() == false) {
      //try to apply its action to nearby book hey
      EnchantStorageTarget target = BlockLibraryController.findMatchingTarget(world, pos, stack);
      if (target.isEmpty() == false) {
        ModCyclic.logger.error(target.library.getPos() + " ? " + target.quad);
        ItemStack theThing = target.library.addEnchantmentToQuadrant(stack, target.quad);
        target.library.markDirty();
        this.setInventorySlotContents(SLOT_IN, ItemStack.EMPTY);
        if (theThing.isEmpty() == false) {
          this.setInventorySlotContents(SLOT_OUT, theThing);
        }
        else {
          //TODO merge 
          this.setInventorySlotContents(SLOT_OUT, new ItemStack(Items.BOOK));
        }
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    return super.writeToNBT(tags);
  }
}
