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
import com.lothrazar.cyclicmagic.block.enchantlibrary.EnchantStack;
import com.lothrazar.cyclicmagic.block.enchantlibrary.QuadrantEnum;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

public class TileEntityLibrary extends TileEntityBaseMachine implements ITickable {

  public static final int MAX_COUNT = 64;
  private static final int HEADER_TIMER = 10;
  private static final String NBT_CLICKED = "lastClicked";
  EnchantStack[] storage = new EnchantStack[QuadrantEnum.values().length];
  QuadrantEnum lastClicked = null;
  private int timer = 0;
  private int displayMode = 0;

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

  public void toggleDisplaysText() {
    this.displayMode = (this.displayMode + 1) % 2;
  }

  public boolean displaysText() {
    //1 means text, 0 is items
    return displayMode == 1;
  }

  public EnchantStack getEnchantStack(QuadrantEnum area) {
    //EnchantStack
    return storage[area.ordinal()];
  }

  public void removeEnchantment(QuadrantEnum area) {
    storage[area.ordinal()].remove();
  }

  public boolean addEnchantment(QuadrantEnum area, Enchantment ench, int level) {
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

  public boolean addEnchantmentFromPlayer(EntityPlayer player, EnumHand hand, QuadrantEnum segment) {
    Enchantment enchToRemove = null;
    ItemStack playerHeld = player.getHeldItem(hand);
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
        //TODO: merge shared with TileENtityDisenchanter
        player.addItemStackToInventory(new ItemStack(Items.BOOK));
        player.setHeldItem(hand, ItemStack.EMPTY);
        player.getCooldownTracker().setCooldown(Items.BOOK, 50);

      }
      else {
        //it has more than one, so downshift by 1
        //cannot edit so just remake the copy with one less
        enchants.remove(enchToRemove);
        ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(enchants, inputCopy);

        //        player.setHeldItem(hand, inputCopy);
        player.addItemStackToInventory(inputCopy);
        player.setHeldItem(hand, ItemStack.EMPTY);
      }
      //        library.markDirty();
      //      onSuccess(player);
      return true;
    }
    return false;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    displayMode = tags.getInteger("displayMode");
    this.timer = tags.getInteger("t");
    if (tags.hasKey(NBT_CLICKED))
      this.lastClicked = QuadrantEnum.values()[tags.getInteger(NBT_CLICKED)];
    for (QuadrantEnum q : QuadrantEnum.values()) {
      EnchantStack s = new EnchantStack();
      s.readFromNBT(tags, q.name());
      storage[q.ordinal()] = s;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger("displayMode", displayMode);
    for (QuadrantEnum q : QuadrantEnum.values()) {
      tags.setTag(q.name(), getEnchantStack(q).writeToNBT());
    }
    if (lastClicked != null) {
      tags.setInteger(NBT_CLICKED, lastClicked.ordinal());
    }
    tags.setInteger("t", timer);
    return super.writeToNBT(tags);
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

  public QuadrantEnum findMatchingQuadrant(ItemStack enchBookStack) {
    for (int i = 0; i < storage.length; i++) {
      if (storage[i].doesMatchNonEmpty(enchBookStack)) {
        return QuadrantEnum.values()[i];
      }
    }
    return null;
  }
}
