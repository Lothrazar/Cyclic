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

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.block.enchantlibrary.EnchantStorageTarget;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityLibraryCtrl extends TileEntityBaseMachineInvo implements ITickable {

  private static final int SLOT_IN = 0;
  private static final int SLOT_OUT = 1;
  private static final int TIMER_MAX = 20;

  public TileEntityLibraryCtrl() {
    super(2);
    this.setSlotsForInsert(SLOT_IN);
    this.setSlotsForExtract(SLOT_OUT);
  }

  @Override
  public void update() {
    if (timer > 0) {
      timer--;
      return;
    }
    //its at zero restart
    timer = TIMER_MAX;
    if (this.getStackInSlot(SLOT_OUT).isEmpty() == false) {
      return;
    }
    ItemStack stackIn = this.getStackInSlot(SLOT_IN);
    if (stackIn.isEmpty()) {
      return;
    }
    //is it an enchanted book 
    if (stackIn.getItem().equals(Items.ENCHANTED_BOOK) == false) {
      //move it to output i dont want it yuky 
      this.setInventorySlotContents(SLOT_OUT, stackIn);
      this.setInventorySlotContents(SLOT_IN, ItemStack.EMPTY);
      return;
    }
    //try to apply its action to nearby book hey
    EnchantStorageTarget target = BlockLibraryController.findMatchingTarget(world, pos, stackIn);
    if (target.isEmpty() == false) {
      //ModCyclic.logger.error(target.library.getPos() + " ? " + target.quad);
      ItemStack theThing = target.library.addEnchantmentToQuadrant(stackIn, target.quad);
      IBlockState oldState = world.getBlockState(target.library.getPos());
      world.notifyBlockUpdate(target.library.getPos(), oldState, oldState, 3);
      this.setInventorySlotContents(SLOT_IN, ItemStack.EMPTY);
      if (theThing.isEmpty() == false) {
        UtilSound.playSound(world, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS);
        //its not empty, but what is it 
        if (theThing.getItem().equals(Items.ENCHANTED_BOOK)) {
          //a book with multi enchants, keep going 
          this.setInventorySlotContents(SLOT_IN, theThing);
        }
        else {
          this.setInventorySlotContents(SLOT_OUT, theThing);
        }
      }
      else {
        this.setInventorySlotContents(SLOT_OUT, new ItemStack(Items.BOOK));
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
