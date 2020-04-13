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
package com.lothrazar.cyclicmagic.block.enchantlibrary.shelf;

import java.util.Map;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.data.EnchantStack;
import com.lothrazar.cyclicmagic.data.QuadrantEnum;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityLibrary extends TileEntityBaseMachine implements ITickable {

  public static final int MAX_COUNT = 999999;
  private static final int HEADER_TIMER = 10;
  private static final String NBT_CLICKED = "lastClicked";
  private EnchantStack[] storage = new EnchantStack[QuadrantEnum.values().length];
  private QuadrantEnum lastClicked = null;
  private int timer = 0;

  public TileEntityLibrary() {
    super();
    for (int i = 0; i < storage.length; i++) {
      storage[i] = new EnchantStack();
    }
  }

  @Override
  public void update() {
    if (this.timer > 0) {
      this.timer--;
    }
    if (this.timer == 0) {
      this.lastClicked = null;
    }
  }

  public EnchantStack getEnchantStack(QuadrantEnum area) {
    //EnchantStack
    return storage[area.ordinal()];
  }

  public void removeEnchantment(QuadrantEnum area) {
    storage[area.ordinal()].remove();
  }

  private boolean addEnchantment(QuadrantEnum area, Enchantment ench, int level) {
    int index = area.ordinal();
    EnchantStack enchStackCurrent = storage[index];
    if (enchStackCurrent.getCount() >= MAX_COUNT) {
      return false;
    }
    if (enchStackCurrent.isEmpty()) {
      enchStackCurrent = new EnchantStack(ench, level);
      storage[index] = enchStackCurrent;
      return true;
    }
    else if (enchStackCurrent.doesMatch(ench, level)) {
      enchStackCurrent.add();
      storage[index] = enchStackCurrent;
      return true;
    }
    else {
      return false;
    }
  }

  public ItemStack addEnchantmentToQuadrant(ItemStack playerHeld, QuadrantEnum segment) {
    Enchantment enchToRemove = null;
    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(playerHeld);
    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
      if (this.addEnchantment(segment, entry.getKey(), entry.getValue())) {
        enchToRemove = entry.getKey();
        break;
      }
    }
    if (enchToRemove != null) {
      // success
      if (enchants.size() == 1) {
        //if it only has 1, and we are going to reomve that last thing, well its just a book now
        return ItemStack.EMPTY;
      }
      else {
        //it has more than one, so downshift by 1
        //cannot edit so just remake the copy with one less
        enchants.remove(enchToRemove);
        ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(enchants, inputCopy);
        refreshTarget();
        return inputCopy;
      }
      //        library.markDirty();
      //      onSuccess(player); 
    }
    //    return false;
    return playerHeld;
  }

  private void refreshTarget() {
    this.markDirty();
    IBlockState oldState = world.getBlockState(getPos());
    world.notifyBlockUpdate(getPos(), oldState, oldState, 3);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.timer = tags.getInteger("t");
    if (tags.hasKey(NBT_CLICKED))
      this.lastClicked = QuadrantEnum.values()[tags.getInteger(NBT_CLICKED)];
    readFromNBTenchants(tags);
  }

  public void readFromNBTenchants(NBTTagCompound tags) {
    for (QuadrantEnum q : QuadrantEnum.values()) {
      EnchantStack s = new EnchantStack();
      s.readFromNBT(tags, q.name());
      storage[q.ordinal()] = s;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    writeToNBTenchants(tags, false);
    if (lastClicked != null) {
      tags.setInteger(NBT_CLICKED, lastClicked.ordinal());
    }
    tags.setInteger("t", timer);
    return super.writeToNBT(tags);
  }

  public NBTTagCompound writeToNBTenchants(NBTTagCompound tags, boolean ignoreBlank) {
    int countQuads = 0;
    for (QuadrantEnum q : QuadrantEnum.values()) {
      EnchantStack st = getEnchantStack(q);
      if (ignoreBlank && st.getCount() == 0) {
        continue;//skip this blank
      }
      tags.setTag(q.name(), st.writeToNBT());
      countQuads++;
    }
    if (countQuads == 0 && ignoreBlank) {
      return null;
    }
    return tags;
  }

  public void setLastClicked(QuadrantEnum segment) {
    this.timer = HEADER_TIMER * Const.TICKS_PER_SEC;
    this.lastClicked = segment;
  }

  public QuadrantEnum getLastClicked() {
    return this.lastClicked;
  }

  public boolean isEmpty() {
    for (int i = 0; i < storage.length; i++) {
      if (storage[i].isEmpty() == false) {
        return false;
      }
    }
    return true;
  }

  public QuadrantEnum findEmptyQuadrant() {
    for (int i = 0; i < storage.length; i++) {
      if (storage[i].isEmpty()) {
        return QuadrantEnum.values()[i];
      }
    }
    return null;
  }

  public QuadrantEnum findMatchingQuadrant(ItemStack enchBookStack, TileEntityLibrary lib) {
    for (int i = 0; i < storage.length; i++) {
      if (storage[i].doesMatchNonEmpty(enchBookStack)) {
        QuadrantEnum quad = QuadrantEnum.values()[i];
        if (lib.getEnchantStack(quad).getCount() < TileEntityLibrary.MAX_COUNT) {
          return quad;
        }
      }
    }
    return null;
  }
}
